package com.example.shopify.data.repositories.wishlist

import com.example.shopify.data.managers.wishlist.IWishlistManager
import com.example.shopify.data.models.ProductSample
import kotlinx.coroutines.flow.SharedFlow

class WishlistRepository(
    private val wishlistManager: IWishlistManager
) : IWishlistRepository {
    override val wishlist: SharedFlow<List<ProductSample>> = wishlistManager.wishlist

    override suspend fun getWishlistItems() =
        wishlistManager.getWishlistItems()

    override suspend fun addWishlistItem(productID: Long, variantID: Long) =
        wishlistManager.addWishlistItem(
            productID = productID,
            variantID = variantID
        )

    override suspend fun removeWishlistItem(productID: Long) =
        wishlistManager.removeWishlistItem(productID = productID)

}