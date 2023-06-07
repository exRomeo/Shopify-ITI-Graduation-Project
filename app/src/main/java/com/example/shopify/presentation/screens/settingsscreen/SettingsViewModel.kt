package com.example.shopify.presentation.screens.settingsscreen

import androidx.lifecycle.ViewModel
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel() : ViewModel() {
    private var _addresses: MutableStateFlow<MutableList<Address>> = MutableStateFlow(
        mutableListOf(
            Address(
                id = 0,
                phoneNumber = "102541521",
                buildingNumber = "Gamaal",
                streetName = "ammar EbnYasser",
                city = "Alexandria",
                zipCode = "5646"
            )
        )
    )
    val addresses = _addresses.asStateFlow()


    private var _orders: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    val orders = _orders.asStateFlow()


    private var _wishlist: MutableStateFlow<List<Product>> = MutableStateFlow(listOf())
    val wishlist = _wishlist.asStateFlow()

    fun updateAddress(address: Address) {
        val index = _addresses.value.indexOfFirst { it.id == address.id }
        if (index >= 0)
            _addresses.value[index] = address

    }

    fun addAddress(address: Address) {
        address.id = _addresses.value.size
        _addresses.value.add(address)
    }

    private var _currencySymbols: MutableStateFlow<List<String>> = MutableStateFlow(listOf())


}