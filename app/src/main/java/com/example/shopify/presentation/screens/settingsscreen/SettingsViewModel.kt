package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.managers.CartManager
import com.example.shopify.data.managers.WishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.user.IUserDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val userDataRepository: IUserDataRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager
) : ViewModel() {

    private var _settingsState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val settingsState = _settingsState.asStateFlow()

    init {
        if (CurrentUserHelper.isLoggedIn()) {
            getAddresses()
            getWishlistItems()
            getCartItems()
            _settingsState.value = UserScreenUISState.LoggedIn
        } else {
            _settingsState.value = UserScreenUISState.NotLoggedIn
        }
    }

    private var _snackbarMessage: MutableSharedFlow<String> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    /**
     * Address Functions
     */

    private var _addresses: MutableStateFlow<List<Address>> = MutableStateFlow(
        listOf()
    )
    val addresses = _addresses.asStateFlow()

    private fun getAddresses() {
        viewModelScope.launch {
            val response = userDataRepository.getAddresses(CurrentUserHelper.customerID)
            if (response.isSuccessful && response.body() != null)
                _addresses.value = response.body()!!.addresses
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.updateAddress(address)
            _snackbarMessage.emit(
                if (response.isSuccessful)
                    "Address has been updated"
                else
                    "Failed to updated address"
            )
            getAddresses()
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            Log.i(TAG, "addAddress: Called")
            val response = userDataRepository.addAddress(CurrentUserHelper.customerID, address)
            _snackbarMessage.emit(
                if (response.isSuccessful)
                    "Address has been added"
                else
                    "Failed to add address"
            )
            getAddresses()
        }
    }

    fun removeAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.removeAddress(address)
            _snackbarMessage.emit(
                if (response.isSuccessful)
                    "Address has been removed"
                else
                    "Failed to remove address"
            )
            getAddresses()
        }
    }

    /**
     * Orders Functions
     */

    private var _orders: MutableStateFlow<List<ProductSample>> = MutableStateFlow(
        listOf()
    )
    val orders = _orders.asStateFlow()

    fun updateOrderItem(product: ProductSample) {
        val index = _orders.value.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            val arr = _orders.value.toMutableList()
            arr[index] = product
            _orders.value = arr
        }
    }

    fun addOrderItem(product: ProductSample) {
        product.id = _orders.value.size.toLong()
        val arr = _orders.value.toMutableList()
        arr.add(product)
        _orders.value = arr
    }

    fun removeOrderItem(product: ProductSample) {
        val arr = _orders.value.toMutableList()
        arr.remove(product)
        _orders.value = arr
    }

    /**
     * Wishlist Functions
     */

    private var _wishlist: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val wishlist = _wishlist.asStateFlow()

    private fun getWishlistItems() {
        viewModelScope.launch {
            wishlistManager.getWishlistItems()
            wishlistManager.wishlist.collect {
                _wishlist.value = it
            }
        }
    }

    fun addWishlistItem(product: ProductSample) {
        viewModelScope.launch {
            wishlistManager.addWishlistItem(product)
        }
    }

    fun removeWishlistItem(product: ProductSample) {
        viewModelScope.launch {
            wishlistManager.removeWishlistItem(product)
        }
    }


    /**
     * Cart Functions
     */

    private var _cart: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val cart = _cart.asStateFlow()


    fun getCartItemCount(product: ProductSample): Long {
        return cartManager.getCartItemCount(product)
    }

    fun increaseCartItemCount(product: ProductSample) {
        viewModelScope.launch {
            cartManager.increaseCartItemCount(product)
        }
    }

    fun decreaseCartItemCount(product: ProductSample) {
        viewModelScope.launch {
            cartManager.decreaseCartItemCount(product)
        }
    }

    private fun getCartItems() {
        viewModelScope.launch {
            cartManager.getCartItems()
            cartManager.cart.collect {
                _cart.value = it
            }
        }
    }

    fun addCartItem(product: ProductSample) {
        viewModelScope.launch {
            cartManager.addCartItem(product)
        }

    }

    fun removeCart(product: ProductSample) {
        viewModelScope.launch {
            cartManager.removeCart(product)
        }
    }
}

class SettingsViewModelFactory(
    private val userDataRepository: IUserDataRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
            SettingsViewModel(userDataRepository, wishlistManager, cartManager) as T
        else
            throw Exception("ViewModel Not Found")
    }
}
