package com.example.shopify.data.repositories.address

import com.example.shopify.data.models.address.Address
import kotlinx.coroutines.flow.SharedFlow

interface IAddressRepository {

    val address: SharedFlow<List<Address>>

    suspend fun getAddresses()

    suspend fun addAddress(address: Address)

    suspend fun updateAddress(address: Address)

    suspend fun removeAddress(address: Address)
}