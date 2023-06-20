package com.example.shopify.data.repositories.user

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import kotlinx.coroutines.flow.SharedFlow

interface IUserDataRepository {
    val address: SharedFlow<List<Address>>
    val wishlist: SharedFlow<List<ProductSample>>
    val cart: SharedFlow<List<ProductSample>>

    /**
     * Address Functions
     */

    suspend fun getAddresses()

    suspend fun addAddress(
        address: Address
    )

    suspend fun updateAddress(
        address: Address
    )

    suspend fun removeAddress(
        address: Address
    )


    suspend fun getWishlistItems()
    suspend fun getCartItems()

}