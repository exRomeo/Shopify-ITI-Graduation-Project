package com.example.shopify.data.repositories.user

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.order.OrderIn
import kotlinx.coroutines.flow.SharedFlow

interface IUserDataRepository {
    val address: SharedFlow<List<Address>>
    val wishlist: SharedFlow<List<ProductSample>>
    val cart: SharedFlow<List<ProductSample>>
    val orders: SharedFlow<List<OrderIn>>

    /**
     * Address Functions
     */

    suspend fun getAddresses()
    suspend fun getWishlistItems()
    suspend fun getCartItems()
    suspend fun  getOrders()
}