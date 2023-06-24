package com.example.shopify.data.repositories.cart

import com.example.shopify.data.managers.cart.ICartManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.resources.FakeLineItems
import com.example.shopify.resources.FakeProducts
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeCartManager : ICartManager {

    private var _cart: MutableSharedFlow<List<ProductSample>> = MutableSharedFlow(1)
    override val cart: SharedFlow<List<ProductSample>> = _cart.asSharedFlow()
    private var count: Long = 2
    override fun getCartItemCount(product: ProductSample): Long = count

    override suspend fun increaseCartItemCount(product: ProductSample) {
        count++
    }

    override suspend fun decreaseCartItemCount(product: ProductSample) {
        count--
    }

    override suspend fun getCartItems() {
        _cart.emit(FakeProducts.productList)
    }

    override suspend fun addCartItem(productID: Long, variantID: Long, quantity: Long) {
        val list: MutableList<ProductSample> = ArrayList(_cart.replayCache[0])
        list.add(FakeProducts.product4)
        _cart.emit(list)
    }

    override suspend fun removeCart(productID: Long) {
        val list: MutableList<ProductSample> = ArrayList(_cart.replayCache[0])
        list.removeIf { it.id == productID }
        _cart.emit(list)
    }

    override fun getLineItems(): List<LineItem> {
        return FakeLineItems.lineItems
    }

    override suspend fun clearCart() {
        _cart.emit(listOf())
    }
}