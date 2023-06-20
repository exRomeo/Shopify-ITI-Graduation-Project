package com.example.shopify.data.repositories.address

import com.example.shopify.data.managers.address.IAddressManager
import com.example.shopify.data.models.address.Address
import kotlinx.coroutines.flow.SharedFlow

class AddressRepository(private val addressManager: IAddressManager) : IAddressRepository {

    override val address: SharedFlow<List<Address>> = addressManager.addresses

    override suspend fun getAddresses() =
        addressManager
            .getAddresses()

    override suspend fun addAddress(address: Address) =
        addressManager.addAddress(
            address = address
        )

    override suspend fun updateAddress(address: Address) =
        addressManager.updateAddress(
            address = address
        )

    override suspend fun removeAddress(address: Address) =
        addressManager.removeAddress(
            address = address
        )
}