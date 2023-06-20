package com.example.shopify.data.managers.address

import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.repositories.user.remote.retrofitclient.CustomerAddressAPI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AddressManager(private val customerAddressAPI: CustomerAddressAPI) : IAddressManager {

    private var _addresses: MutableSharedFlow<List<Address>> = MutableSharedFlow(1)
    override val addresses = _addresses.asSharedFlow()

    override suspend fun getAddresses() {
        val response = customerAddressAPI.getAddresses(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = CurrentUserHelper.customerID
        )
        if (response.isSuccessful && response.body() != null)
            _addresses.emit(response.body()!!.addresses)
    }

    override suspend fun updateAddress(address: Address) {
        val response = customerAddressAPI.updateAddress(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = CurrentUserHelper.customerID,
            addressID = address.id,
            address = AddressBody(address)
        )
        getAddresses()
    }

    override suspend fun addAddress(address: Address) {
        val response = customerAddressAPI.addAddress(
            BuildConfig.ACCESS_TOKEN,
            CurrentUserHelper.customerID,
            AddressBody(address)
        )
        getAddresses()
    }

    override suspend fun removeAddress(address: Address) {
        val response = customerAddressAPI.removeAddress(
            BuildConfig.ACCESS_TOKEN,
            CurrentUserHelper.customerID,
            address.id
        )
        getAddresses()
    }

}