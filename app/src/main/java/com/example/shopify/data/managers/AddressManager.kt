package com.example.shopify.data.managers

import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.repositories.user.remote.retrofitclient.CustomerAddressAPI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AddressManager(private val customerAddressAPI: CustomerAddressAPI) {

    private var _addresses: MutableSharedFlow<List<Address>> = MutableSharedFlow(1)
    val addresses = _addresses.asSharedFlow()

    private suspend fun getAddresses() {
        val response = customerAddressAPI.getAddresses(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = CurrentUserHelper.customerID
        )
        if (response.isSuccessful && response.body() != null)
            _addresses.emit(response.body()!!.addresses)
    }

    suspend fun updateAddress(address: Address) {
        val response = customerAddressAPI.updateAddress(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = CurrentUserHelper.customerID,
            addressID = address.id,
            address = AddressBody(address)
        )
        getAddresses()
    }

    suspend fun addAddress(address: Address) {
        val response = customerAddressAPI.addAddress(
            BuildConfig.ACCESS_TOKEN,
            CurrentUserHelper.customerID,
            AddressBody(address)
        )
        getAddresses()
    }

    suspend fun removeAddress(address: Address) {
        val response = customerAddressAPI.removeAddress(
            BuildConfig.ACCESS_TOKEN,
            CurrentUserHelper.customerID,
            address.id
        )
        getAddresses()
    }

}