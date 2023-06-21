package com.example.shopify.presentation.screens.cartscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.cart.ICartRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: ICartRepository) : ViewModel() {

    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _screenState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val screenState = _screenState.asStateFlow()


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
        if (ConnectionUtil.isConnected()) {
            viewModelScope.launch {
                cartRepository.getCartItems()
                cartRepository.cart.collect {
                    if (_cart != null) {
                        _cart.value = it
                        _screenState.value = UserScreenUISState.Success(it)

                    } else {
                        _screenState.value = UserScreenUISState.NoData
                    }
                }
            }
        } else
            _screenState.value = UserScreenUISState.NotConnected
    }


    fun removeCart(productID: Long) {
        viewModelScope.launch {
            cartRepository.removeCart(productID = productID)
        }
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