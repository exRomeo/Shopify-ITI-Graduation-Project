package com.example.shopify.presentation.screens.cartscreen

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.cart.ICartRepository
import com.example.shopify.resources.FakeProducts
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FakeCartRepository : ICartRepository {
    private var _cart: MutableSharedFlow<List<ProductSample>> = MutableSharedFlow(1)
    override val cart: SharedFlow<List<ProductSample>> = _cart.asSharedFlow()
    private var count: Long = 2

    override fun getCartItemCount(product: ProductSample): Long = count

    private val currentList = FakeProducts.productList
//    init {
//        GlobalScope.launch {
//            getCartItems() }
//    }
    override suspend fun increaseCartItemCount(product: ProductSample) {
        count++
    }

    override suspend fun decreaseCartItemCount(product: ProductSample) {
        count--
    }

    override suspend fun getCartItems() {
        _cart.emit(currentList)
    }

    override suspend fun addCartItem(productID: Long, variantID: Long, quantity: Long) {
        currentList.add(FakeProducts.product4)
        _cart.emit(currentList)
    }

    override suspend fun removeCart(productID: Long) {
        currentList.removeIf { it.id == productID }
        _cart.emit(currentList)
    }

}