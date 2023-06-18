package com.example.shopify.data.managers.cart

import com.example.shopify.data.models.ProductSample
import kotlinx.coroutines.flow.SharedFlow

interface ICartManager {

    val cart: SharedFlow<List<ProductSample>>

    fun getCartItemCount(product: ProductSample): Long

    suspend fun increaseCartItemCount(product: ProductSample)

    suspend fun decreaseCartItemCount(product: ProductSample)

    suspend fun getCartItems()

    suspend fun addCartItem(productID: Long, variantID: Long, quantity: Long = 1)

    suspend fun removeCart(productID: Long)
}