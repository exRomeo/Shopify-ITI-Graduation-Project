package com.example.shopify.data.managers.wishlist

import android.util.Log
import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.draftorder.DraftOrderBody
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.repositories.user.remote.retrofitclient.DraftOrderAPI
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class WishlistManager(
    private val draftOrderAPI: DraftOrderAPI
) : IWishlistManager {
    private var _wishlist: MutableSharedFlow<List<ProductSample>> = MutableSharedFlow(1)
    override val wishlist: SharedFlow<List<ProductSample>> = _wishlist.asSharedFlow()
    private lateinit var wishlistDraftOrder: DraftOrderBody

    override suspend fun getWishlistItems() {
        if (CurrentUserHelper.hasWishlist()) {
            val response =
                draftOrderAPI.getDraftOrder(
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

    override suspend fun addWishlistItem(productID: Long, variantID: Long) {
        if (!::wishlistDraftOrder.isInitialized)
            getWishlistItems()
        if (CurrentUserHelper.hasWishlist())
            addToWishlistDraftOrder(productID = productID, variantID = variantID)
        else
            createWishlist(productID = productID, variantID = variantID)
        updateWishlist()
        getWishlistItems()
    }

    private suspend fun updateWishlist() {
        draftOrderAPI.updateDraftOrder(
            accessToken = BuildConfig.ACCESS_TOKEN,
            draftOrderID = wishlistDraftOrder.draftOrder.id,
            draftOrderBody = DraftOrderBody(wishlistDraftOrder.draftOrder)
        )
    }

    private suspend fun addToWishlistDraftOrder(productID: Long, variantID: Long) {
        if (!::wishlistDraftOrder.isInitialized)
            getWishlistItems()
        wishlistDraftOrder.draftOrder.lineItems.add(
            element = LineItem(
                variantID = variantID,
                productID = productID,
                title = "product.title",
                quantity = 1,
                name = "product.title",
                price = ""
            )
        )
    }

    private suspend fun createWishlist(productID: Long, variantID: Long) {
        wishlistDraftOrder = DraftOrderBody(
            DraftOrder(
                id = 0L,
                note = ">wishlist<",
                lineItems = mutableListOf(
                    LineItem(
                        variantID = variantID,
                        productID = productID,
                        title = "product.title",
                        quantity = 1,
                        name = "product.title",
                        price = ""
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

    override suspend fun removeWishlistItem(productID: Long) {
        if (wishlistDraftOrder.draftOrder.lineItems.size > 1) {
            val index: Int = wishlistDraftOrder.draftOrder.lineItems.indexOfFirst {
                it.productID == productID
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

     override suspend fun isFavorite(productID: Long/*, variantID: Long*/): Boolean {
         return if (!::wishlistDraftOrder.isInitialized) {
             getWishlistItems()
             false
         } else{
             getWishlistItems()
             wishlistDraftOrder.draftOrder.lineItems.any {
                 it.productID == productID/* && it.variantID == variantID*/
             }
         }

    }
}