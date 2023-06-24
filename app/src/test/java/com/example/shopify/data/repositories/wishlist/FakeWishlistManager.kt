package com.example.shopify.data.repositories.wishlist

import com.example.shopify.data.managers.wishlist.IWishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.resources.FakeProducts
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeWishlistManager : IWishlistManager {

    private var _wishlist: MutableSharedFlow<List<ProductSample>> = MutableSharedFlow(1)
    override val wishlist: SharedFlow<List<ProductSample>> = _wishlist.asSharedFlow()


    override suspend fun getWishlistItems() {

        _wishlist.emit(FakeProducts.productList)
    }

    override suspend fun addWishlistItem(productID: Long, variantID: Long) {
        val list: MutableList<ProductSample> = ArrayList(_wishlist.replayCache[0])
        list.add(FakeProducts.product4)
        _wishlist.emit(list)
    }

    override suspend fun removeWishlistItem(productID: Long) {
        val list: MutableList<ProductSample> = ArrayList(_wishlist.replayCache[0])
        list.removeIf { it.id == productID }
        _wishlist.emit(list)
    }

    override suspend fun isFavorite(productID: Long): Boolean {

        val list: MutableList<ProductSample> = ArrayList(_wishlist.replayCache[0])
        return list.any { it.id == productID }
    }
}