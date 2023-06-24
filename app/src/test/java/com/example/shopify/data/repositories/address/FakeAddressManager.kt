package com.example.shopify.data.repositories.address

import com.example.shopify.data.managers.address.IAddressManager
import com.example.shopify.data.models.address.Address
import com.example.shopify.resources.FakeAddress
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeAddressManager : IAddressManager {
    private var _addresses: MutableSharedFlow<List<Address>> = MutableSharedFlow(1)
    override val addresses: SharedFlow<List<Address>> = _addresses.asSharedFlow()

    override suspend fun getAddresses() {
        _addresses.emit(FakeAddress.addressList)
    }

    override suspend fun updateAddress(address: Address) {
        val addressList = ArrayList(_addresses.replayCache[0])
        addressList.replaceAll {
            if (it.id == address.id)
                address
            else
                it
        }
        _addresses.emit(addressList)
    }

    override suspend fun addAddress(address: Address) {
        val addressList = ArrayList(_addresses.replayCache[0])
        addressList.add(address)
        _addresses.emit(addressList)

    }

    override suspend fun removeAddress(address: Address) {
        val addressList = ArrayList(_addresses.replayCache[0])
        addressList.removeAll { it.id == address.id }
        _addresses.emit(addressList)
    }
}