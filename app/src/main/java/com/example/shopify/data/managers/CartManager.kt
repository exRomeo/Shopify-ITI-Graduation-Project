package com.example.shopify.data.managers

import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.draftorder.DraftOrderBody
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.repositories.user.remote.retrofitclient.DraftOrderAPI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CartManager(
    private val draftOrderAPI: DraftOrderAPI
) {
    private var _cart: MutableSharedFlow<List<ProductSample>> = MutableSharedFlow(1)
    val cart: SharedFlow<List<ProductSample>> = _cart.asSharedFlow()
    private lateinit var cartDraftOrder: DraftOrderBody

    fun getCartItemCount(product: ProductSample): Long {
        var index = cart.replayCache.first().indexOf(product)
        while (index > cartDraftOrder.draftOrder.lineItems.size) {
            index--
        }
        return cartDraftOrder.draftOrder.lineItems[index].quantity
    }

    suspend fun increaseCartItemCount(product: ProductSample) {
        if (getCartItemCount(product) < product.variants[0].availableAmount!!) {
            cartDraftOrder.draftOrder.lineItems[cart.replayCache.first().indexOf(product)].quantity++
            updateCart()
        }
    }

    suspend fun decreaseCartItemCount(product: ProductSample) {
        cartDraftOrder.draftOrder.lineItems[cart.replayCache.first().indexOf(product)].quantity--
        updateCart()
    }

    suspend fun getCartItems() {
        if (CurrentUserHelper.hasCart()) {
            val response =
                draftOrderAPI.getDraftOrder(
                    accessToken = BuildConfig.ACCESS_TOKEN,
                    draftOrderID = CurrentUserHelper.cartDraftID
                )
            if (response.isSuccessful && response.body() != null) {
                cartDraftOrder = response.body()!!
                val draftOrderItems = cartDraftOrder.draftOrder.lineItems
                _cart.emit(getProductsFromLineItems(draftOrderItems))
            } else {
                CurrentUserHelper.updateListID(KeyFirebase.card_id, -1)
            }
        }
    }

    private suspend fun getProductsFromLineItems(lineItems: List<LineItem>): List<ProductSample> {
        val lineItemsCopy = ArrayList(lineItems)
        return lineItemsCopy.mapNotNull { lineItem ->
            val productResponse =
                draftOrderAPI.getProductByID(
                    accessToken = BuildConfig.ACCESS_TOKEN,
                    productID = lineItem.productID
                )
            productResponse.body()?.product
        }
    }

    suspend fun addCartItem(productID: Long, variantID: Long) {
        if (!::cartDraftOrder.isInitialized)
            getCartItems()
        if (CurrentUserHelper.hasCart())
            addToCartDraftOrder(productID = productID, variantID = variantID)
        else
            createCart(productID = productID, variantID = variantID)
        updateCart()
        getCartItems()
    }

    private suspend fun updateCart() {
        draftOrderAPI.updateDraftOrder(
            accessToken = BuildConfig.ACCESS_TOKEN,
            draftOrderID = cartDraftOrder.draftOrder.id,
            draftOrderBody = DraftOrderBody(cartDraftOrder.draftOrder)
        )
    }

    private suspend fun addToCartDraftOrder(productID: Long, variantID: Long) {
        if (!::cartDraftOrder.isInitialized)
            getCartItems()
        cartDraftOrder.draftOrder.lineItems.add(
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


    private suspend fun createCart(productID: Long, variantID: Long) {
        cartDraftOrder = DraftOrderBody(
            DraftOrder(
                id = 0L,
                note = ">Cart<",
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
            draftOrderBody = cartDraftOrder
        )

        CurrentUserHelper.updateListID(
            listType = KeyFirebase.card_id,
            response.body()?.draftOrder?.id ?: -1L
        )
    }


    suspend fun removeCart(productID: Long) {
        if (cartDraftOrder.draftOrder.lineItems.size > 1) {
            val index: Int = cartDraftOrder.draftOrder.lineItems.indexOfFirst {
                it.productID == productID
            }
            cartDraftOrder.draftOrder.lineItems.removeAt(index)
            draftOrderAPI.updateDraftOrder(
                accessToken = BuildConfig.ACCESS_TOKEN,
                draftOrderID = cartDraftOrder.draftOrder.id,
                draftOrderBody = cartDraftOrder
            )
            getCartItems()
        } else {
            deleteDraftOrder(
                draftOrder = cartDraftOrder.draftOrder,
                draftOrderType = KeyFirebase.card_id
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
        _cart.emit(listOf())
        cartDraftOrder.draftOrder.lineItems = mutableListOf()
    }
}