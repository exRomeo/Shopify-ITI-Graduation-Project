package com.example.shopify.data.managers.address

import com.example.shopify.data.models.address.Address
import kotlinx.coroutines.flow.SharedFlow

interface IAddressManager {
    val addresses: SharedFlow<List<Address>>
    suspend fun getAddresses()

    suspend fun updateAddress(address: Address)

    suspend fun addAddress(address: Address)

    suspend fun removeAddress(address: Address)
}