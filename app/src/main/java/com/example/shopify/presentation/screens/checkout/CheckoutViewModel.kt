package com.example.shopify.presentation.screens.checkout

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.models.order.Customer
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderOut
import com.example.shopify.data.repositories.checkout.ICheckoutRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CheckoutViewModel(private val checkoutRepository: ICheckoutRepository) : ViewModel() {
    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

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
            checkoutRepository.makeOrder(
                OrderBody(
                    OrderOut(
                        shippingAddress = chosenAddress,
                        lineItems = checkoutRepository.getCart(),
                        customer = Customer(id = chosenAddress.customerID)
                    )
                )
            )
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
}

class CheckoutViewModelFactory(private val checkoutRepository: ICheckoutRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CheckoutViewModel::class.java))
            CheckoutViewModel(checkoutRepository) as T
        else throw Exception("View Model Not Found!")
    }
}