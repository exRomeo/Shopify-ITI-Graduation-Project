package com.example.shopify.presentation.screens.checkout

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.models.order.Customer
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderOut
import com.example.shopify.data.repositories.checkout.ICheckoutRepository
import com.stripe.android.paymentsheet.PaymentSheetResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CheckoutViewModel(private val checkoutRepository: ICheckoutRepository) : ViewModel() {
    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _forceNav: MutableSharedFlow<Screens> = MutableSharedFlow()
    val forceNav = _forceNav.asSharedFlow()

    private var _state: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val state = _state.asStateFlow()

    private var _addresses: MutableStateFlow<List<Address>> =
        MutableStateFlow(listOf())
    val addresses = _addresses.asStateFlow()

    private var lineItems: List<LineItem> = listOf()

    private var _totalItems: MutableStateFlow<String> = MutableStateFlow("")
    val totalItems = _totalItems.asStateFlow()

    private var _totalPrice: MutableStateFlow<Double> = MutableStateFlow(0.00)
    val totalPrice = _totalPrice.asStateFlow()

    var currencySymbol: String = "EGP"

    private var currencyRate: Double = 1.00
    private lateinit var chosenAddress: Address

    init {
        getCartItems()
        getAddresses()
    }

    private fun getCartItems() {
        if (ConnectionUtil.isConnected()) {
            viewModelScope.launch {
                lineItems = checkoutRepository.getCart()
                if (lineItems.isNotEmpty()) {
                    calculatePrice()
                    _state.value = UserScreenUISState.Success(lineItems)
                } else {
                    _state.value = UserScreenUISState.NoData
                }
            }

        } else
            _state.value = UserScreenUISState.NotConnected
    }

    private fun calculatePrice() {
        var itemsCount = 0L
        var itemsPrice = 0.00
        lineItems.forEach {
            itemsCount += it.quantity
            itemsPrice += (it.price).trim().toDouble() * it.quantity.toDouble() * currencyRate
        }
        _totalItems.value = "Total Items = $itemsCount item(s)"
        _totalPrice.value = (itemsPrice * 100).roundToInt() / 100.00
    }

    fun getExchangeRate(symbol: String) {
        viewModelScope.launch {
            val response =
                checkoutRepository.exchangeRate(to = symbol, from = "EGP", amount = "1")
            if (response.isSuccessful) {
                currencySymbol = symbol
                currencyRate = response.body()?.info?.rate!!
                calculatePrice()
            }
        }
    }


    fun confirmOrder() {
        viewModelScope.launch {
            showMessage(R.string.payment_succeeded)
            checkoutRepository.makeOrder(
                OrderBody(
                    OrderOut(
                        shippingAddress = chosenAddress,
                        lineItems = checkoutRepository.getCart(),
                        customer = Customer(id = chosenAddress.customerID)
                    )
                )
            )
            checkoutRepository.clearCart()
            delay(500)
            forceNavigate(Screens.Home)
        }
    }


    fun getAddresses() {
        viewModelScope.launch {
            checkoutRepository.getCustomerAddresses()
            checkoutRepository.addresses.collect {
                _addresses.value = it
                chosenAddress = it.find { address -> address.default }!!
            }
        }
    }

    fun chooseAddress(address: Address) {
        chosenAddress = address
    }


    fun showMessage(@StringRes message: Int) {
        viewModelScope.launch {
            _snackbarMessage.emit(message)
        }
    }


    fun forceNavigate(destination: Screens) {
        viewModelScope.launch {
            _forceNav.emit(destination)
        }
    }

    /**
     * Payment
     * */
    private var paymentMethod: PaymentMethod = PaymentMethod.Credit
    private val _clientSecret = MutableStateFlow<String?>(null)
    val clientSecret = _clientSecret.asStateFlow()

    init {
        viewModelScope.launch {
            checkoutRepository.createCustomer()
        }
    }

    fun paymentMethod(paymentMethod: PaymentMethod) {
        this.paymentMethod = paymentMethod
    }


    fun confirmPayment() {
        when (paymentMethod) {
            PaymentMethod.Credit -> {
                makePayment()
            }

            PaymentMethod.Cod -> {
                confirmOrder()
            }
        }
    }

    fun makePayment() {
        viewModelScope.launch {
            val paymentIntent = checkoutRepository.createPaymentIntent(totalPrice.value)
            _clientSecret.update { paymentIntent.clientSecret }
        }

    }

    fun onPaymentLaunched() {
        _clientSecret.update { null }
    }

    fun handlePaymentResult(result: PaymentSheetResult) {
        when (result) {
            is PaymentSheetResult.Canceled -> showMessage(R.string.payment_canceled)

            is PaymentSheetResult.Completed -> {
                showMessage(R.string.payment_succeeded)
                confirmOrder()
            }

            is PaymentSheetResult.Failed -> showMessage(R.string.payment_failed)
        }
    }
}

class CheckoutViewModelFactory(private val checkoutRepository: ICheckoutRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CheckoutViewModel::class.java))
            CheckoutViewModel(checkoutRepository) as T
        else throw Exception("View Model Not Found!")
    }
}

enum class PaymentMethod(val value: Int) {
    Credit(1),
    Cod(0)
}