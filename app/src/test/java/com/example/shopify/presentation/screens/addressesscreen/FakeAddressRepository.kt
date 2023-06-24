package com.example.shopify.presentation.screens.addressesscreen

import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.address.IAddressRepository
import com.example.shopify.resources.FakeAddress
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeAddressRepository:IAddressRepository {

    private var _addresses: MutableSharedFlow<List<Address>> = MutableSharedFlow(1)

    override val address: SharedFlow<List<Address>> =_addresses.asSharedFlow()

    private val currentList = FakeAddress.addressList
    override suspend fun getAddresses() {
        _addresses.emit(currentList)
    }

    override suspend fun updateAddress(address: Address) {

        currentList.replaceAll {
            if (it.id == address.id)
                address
            else
                it
        }
        _addresses.emit(currentList)
    }

    override suspend fun addAddress(address: Address) {
        currentList.add(address)
        _addresses.emit(currentList)

    }

    override suspend fun removeAddress(address: Address) {
        currentList.removeAll { it.id == address.id }
        _addresses.emit(currentList)
    }
}