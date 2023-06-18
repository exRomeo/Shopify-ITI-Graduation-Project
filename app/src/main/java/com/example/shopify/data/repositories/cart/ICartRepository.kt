package com.example.shopify.data.repositories.cart

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.currency.CurrencyResponse
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

interface ICartRepository {
    val cart: SharedFlow<List<ProductSample>>

    fun getCartItemCount(product: ProductSample):Long

    suspend fun increaseCartItemCount(product: ProductSample)

    suspend fun decreaseCartItemCount(product: ProductSample)

    suspend fun getCartItems()

    suspend fun addCartItem(productID: Long, variantID: Long, quantity: Long = 1)

    suspend fun removeCart(productID: Long)


    suspend fun exchangeRate(to: String, from: String, amount: String): Response<CurrencyResponse>
}