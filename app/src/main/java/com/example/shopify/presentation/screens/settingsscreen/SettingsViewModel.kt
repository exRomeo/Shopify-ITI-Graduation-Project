package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.user.IUserDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(
    private val userDataRepository: IUserDataRepository
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
        if (ConnectionUtil.isConnected()) {
            if (CurrentUserHelper.isLoggedIn()) {
                getAddresses()
                getWishlistItems()
                getCartItems()
                getOrders()
                _settingsState.value = UserScreenUISState.LoggedIn
            } else {
                _settingsState.value = UserScreenUISState.NotLoggedIn
            }
        }
        _settingsState.value = UserScreenUISState.NotConnected
    }


    private var _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()


    /**
     * Address Functions
     */

    private var _addresses: MutableStateFlow<List<Address>> =
        MutableStateFlow(listOf())

    val addresses = _addresses.asStateFlow()

    private fun getAddresses() {
        viewModelScope.launch {
            userDataRepository.getAddresses()
            userDataRepository.address.collect {
                if (_addresses != null) {
                    _addresses.value = it
                    setUserName()
                } else {
                    _addresses = MutableStateFlow(listOf())
                }
            }
        }
    }


    private fun setUserName() {
        if (_userName.value.isEmpty()) {
            val address = _addresses.value[0]
            _userName.value = "${address.firstName} ${address.lastName}"
        }
    }


    /**
     * Orders Functions
     */

    private var _orders: MutableStateFlow<Int> = MutableStateFlow(
        0
    )
    val orders = _orders.asStateFlow()

    private fun getOrders() {
        viewModelScope.launch {
            userDataRepository.getOrders()
            userDataRepository.orders.collect {
                if (_orders != null)
                    _orders.value = it.size
                else
                    _orders = MutableStateFlow(0)

            }
        }
    }

    /**
     * Wishlist Functions
     */

    private var _wishlist: MutableStateFlow<Int> = MutableStateFlow(0)
    val wishlist = _wishlist.asStateFlow()

    private fun getWishlistItems() {
        viewModelScope.launch {
            userDataRepository.getWishlistItems()
            userDataRepository.wishlist.collect {
                if (_wishlist != null)
                    _wishlist.value = it.size
                else
                    _wishlist = MutableStateFlow(0)
            }
        }
    }


    /**
     * Cart Size
     */


    private var _cart: MutableStateFlow<Int> = MutableStateFlow(0)
    val cart = _cart.asStateFlow()
    private fun getCartItems() {
        viewModelScope.launch {
            userDataRepository.getCartItems()
            userDataRepository.cart.collect {
                if (_cart != null)
                    _cart.value = it.size
                else
                    _cart = MutableStateFlow(0)
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
           val isLogged = CurrentUserHelper.logout()
            if(isLogged)
                Log.i(TAG, "logout: Done")
            else
                Log.i(TAG, "logout: Not Loggedout")
        }
    }
}

class SettingsViewModelFactory(
    private val userDataRepository: IUserDataRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
            SettingsViewModel(userDataRepository) as T
        else
            throw Exception("ViewModel Not Found")
    }
}
