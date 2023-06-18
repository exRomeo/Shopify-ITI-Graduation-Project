package com.example.shopify.presentation.screens.cartscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.cart.ICartRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CartViewModel(private val cartRepository: ICartRepository) : ViewModel() {

    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _screenState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val screenState = _screenState.asStateFlow()


    private var currencySymbol: String = "EGP"

    private var currencyRate: Double = 1.00

    private var _cart: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val cart = _cart.asStateFlow()

    init {
        getCartItems()
    }

    fun getCartItemCount(product: ProductSample): Long {
        return cartRepository.getCartItemCount(product)
    }

    fun increaseCartItemCount(product: ProductSample) {
        viewModelScope.launch {
            if (getCartItemCount(product) < product.variants[0].availableAmount!!)
                cartRepository.increaseCartItemCount(product)
            else
                _snackbarMessage.emit(R.string.exceeded_max_amount)
        }
    }

    fun decreaseCartItemCount(product: ProductSample) {
        viewModelScope.launch {
            cartRepository.decreaseCartItemCount(product)
        }
    }

    private fun getCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems()
            cartRepository.cart.collect {
                _cart.value = it
                _screenState.value = UserScreenUISState.Success(it)
            }
        }
    }

    fun addCartItem(productID: Long, variantID: Long) {
        viewModelScope.launch {
            cartRepository.addCartItem(productID = productID, variantID = variantID)
        }

    }

    fun removeCart(productID: Long) {
        viewModelScope.launch {
            cartRepository.removeCart(productID = productID)
        }
    }

    fun getExchangeRate(symbol: String) {
        viewModelScope.launch {
            val response =
                cartRepository.exchangeRate(to = symbol, from = "EGP", amount = "1")
            if (response.isSuccessful) {
                currencySymbol = symbol
                currencyRate = response.body()?.info?.rate!!
                calculatePrice()
            }
        }
    }

    private var _totalItems: MutableStateFlow<String> = MutableStateFlow("")
    val totalItems = _totalItems.asStateFlow()

    private var _totalPrice: MutableStateFlow<String> = MutableStateFlow("")
    val totalPrice = _totalPrice.asStateFlow()

    fun calculatePrice() {
        var itemsCount = 0L
        var itemsPrice = 0.00
        _cart.value.forEach {
            itemsCount += getCartItemCount(it)
            itemsPrice += ((it.variants[0].price)?.trim()?.toDouble()
                ?: 0.00) * getCartItemCount(it).toDouble() * currencyRate
        }
        _totalItems.value = "Total Items = $itemsCount item(s)"
        _totalPrice.value =
            "Total Price = $currencySymbol ${(itemsPrice * 100).roundToInt() / 100.00}"
    }
}

@Suppress("UNCHECKED_CAST")
class CartViewModelFactory(private val cartRepository: ICartRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CartViewModel::class.java))
            CartViewModel(cartRepository) as T
        else
            throw Exception("View model not found!")
    }
}