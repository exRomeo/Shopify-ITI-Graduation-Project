package com.example.shopify.data.managers

import android.util.Log
import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.draftorder.DraftOrderBody
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.repositories.user.remote.retrofitclient.DraftOrderAPI
import com.example.shopify.presentation.screens.settingsscreen.TAG
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class WishlistManager(
    private val draftOrderAPI: DraftOrderAPI
) {
    private var _wishlist: MutableSharedFlow<List<ProductSample>> = MutableSharedFlow()
    val wishlist: SharedFlow<List<ProductSample>> = _wishlist.asSharedFlow()
    private lateinit var wishlistDraftOrder: DraftOrderBody

    suspend fun getWishlistItems() {
        if (CurrentUserHelper.hasWishlist()) {
            val response = draftOrderAPI.getDraftOrder(
                accessToken = BuildConfig.ACCESS_TOKEN,
                draftOrderID = CurrentUserHelper.wishlistDraftID
            )
            if (response.isSuccessful && response.body() != null) {
                wishlistDraftOrder = response.body()!!
                val draftOrderItems = wishlistDraftOrder.draftOrder.lineItems
                _wishlist.emit(getProductsFromLineItems(draftOrderItems))
            } else {
                CurrentUserHelper.updateListID(KeyFirebase.wishlist_id, -1)
            }
        }
    }

    private suspend fun getProductsFromLineItems(lineItems: List<LineItem>): List<ProductSample> {
        val lineItemsCopy = ArrayList(lineItems)
        return lineItemsCopy.mapNotNull { lineItem ->
            val productResponse = draftOrderAPI.getProductByID(
                accessToken = BuildConfig.ACCESS_TOKEN,
                productID = lineItem.productID
            )
            productResponse.body()?.product
        }
    }

    suspend fun addWishlistItem(product: ProductSample) {
        if (!::wishlistDraftOrder.isInitialized)
            getWishlistItems()
        if (CurrentUserHelper.hasWishlist())
            addToWishlistDraftOrder(product = product)
        else
            createWishlist(product = product)
        updateWishlist()
        getWishlistItems()
    }

    private suspend fun updateWishlist() {
        draftOrderAPI.updateDraftOrder(
            draftOrderID = wishlistDraftOrder.draftOrder.id,
            accessToken = BuildConfig.ACCESS_TOKEN,
            draftOrderBody = DraftOrderBody(wishlistDraftOrder.draftOrder)
        )
    }

    private suspend fun addToWishlistDraftOrder(product: ProductSample) {
        if (!::wishlistDraftOrder.isInitialized)
            getWishlistItems()
        wishlistDraftOrder.draftOrder.lineItems.add(
            element = LineItem(
                variantID = product.variants[0].id,
                productID = product.id,
                title = product.title,
                quantity = 1,
                name = product.title,
                price = product.variants[0].price ?: "0.00"
            )
        )
    }

    private suspend fun createWishlist(product: ProductSample) {
        wishlistDraftOrder = DraftOrderBody(
            DraftOrder(
                id = 0L,
                note = ">wishlist<",
                lineItems = mutableListOf(
                    LineItem(
                        productID = product.id,
                        variantID = product.variants[0].id,
                        title = product.title,
                        name = product.variants[0].title ?: "",
                        price = product.variants[0].price ?: "",
                        quantity = 1L
                    )
                ),
                totalPrice = ""
            )
        )

        val response = draftOrderAPI.createDraftOrder(
            accessToken = BuildConfig.ACCESS_TOKEN,
            draftOrderBody = wishlistDraftOrder
        )

        CurrentUserHelper.updateListID(
            listType = KeyFirebase.wishlist_id,
            newID = response.body()?.draftOrder?.id ?: -1L
        )
    }

    suspend fun removeWishlistItem(product: ProductSample) {
        if (wishlistDraftOrder.draftOrder.lineItems.size > 1) {
            val index: Int = wishlistDraftOrder.draftOrder.lineItems.indexOfFirst {
                it.productID == product.id
            }
            wishlistDraftOrder.draftOrder.lineItems.removeAt(index)
            draftOrderAPI.updateDraftOrder(
                accessToken = BuildConfig.ACCESS_TOKEN,
                draftOrderID = wishlistDraftOrder.draftOrder.id,
                draftOrderBody = wishlistDraftOrder
            )
            getWishlistItems()
        } else {
            deleteDraftOrder(
                draftOrder = wishlistDraftOrder.draftOrder,
                draftOrderType = KeyFirebase.wishlist_id
            )
        }
    }

    private suspend fun deleteDraftOrder(draftOrder: DraftOrder, draftOrderType: KeyFirebase) {
        Log.i(TAG, "deleteDraftOrder: WISHY LIST")
        CurrentUserHelper.updateListID(
            listType = draftOrderType,
            newID = -1
        )
        draftOrderAPI.deleteDraftOrder(
            accessToken = BuildConfig.ACCESS_TOKEN,
            draftOrderID = draftOrder.id
        )
        clearList()
    }

    private suspend fun clearList() {
        _wishlist.emit(listOf())
        wishlistDraftOrder.draftOrder.lineItems = mutableListOf()
    }
}