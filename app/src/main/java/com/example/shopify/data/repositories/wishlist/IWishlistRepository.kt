package com.example.shopify.data.repositories.wishlist

import com.example.shopify.data.models.ProductSample
import kotlinx.coroutines.flow.SharedFlow

interface IWishlistRepository {
    val wishlist: SharedFlow<List<ProductSample>>

    suspend fun getWishlistItems()

    suspend fun addWishlistItem(productID: Long, variantID: Long)

    suspend fun removeWishlistItem(productID: Long)
    suspend fun isFavorite(productID: Long, variantID: Long)
}