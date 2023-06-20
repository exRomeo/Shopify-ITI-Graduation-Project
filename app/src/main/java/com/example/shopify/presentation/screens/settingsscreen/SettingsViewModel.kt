package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.user.IUserDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val settingsState: StateFlow<UserScreenUISState> by lazy {
        MutableStateFlow(
            if (CurrentUserHelper.isLoggedIn())
                UserScreenUISState.LoggedIn
            else
                UserScreenUISState.NotLoggedIn
        )
    }

    init {
        if(ConnectionUtil.isConnected()){
            if (CurrentUserHelper.isLoggedIn()) {
                getAddresses()
                getWishlistItems()
                getCartItems()
                _settingsState.value = UserScreenUISState.LoggedIn
            } else {
                _settingsState.value = UserScreenUISState.NotLoggedIn
            }
        }
        _settingsState.value = UserScreenUISState.NotConnected
    }

    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()


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
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!.addresses
                _addresses.value = data
                _userName.value = "${data[0].firstName} ${data[0].lastName}"
            }
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.updateAddress(address)
            _snackbarMessage.emit(
                if (response.isSuccessful)
                    R.string.address_updated
                else
                    R.string.address_not_updated
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
                    R.string.address_added
                else
                    R.string.address_not_added
            )
            getAddresses()
        }
    }

    fun removeAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.removeAddress(address)
            _snackbarMessage.emit(
                if (response.isSuccessful)
                    R.string.address_removed
                else
                    R.string.address_not_removed
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


    /**
     * Cart Functions
     */


    private var _cart: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val cart = _cart.asStateFlow()
    private fun getCartItems() {
        viewModelScope.launch {
            cartManager.getCartItems()
            cartManager.cart.collect {
                _cart.value = it
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            CurrentUserHelper.logout()
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
