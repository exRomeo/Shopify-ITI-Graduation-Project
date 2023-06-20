package com.example.shopify.data.repositories.checkout

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.currency.CurrencyResponse
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.payment.PaymentIntentApiModel
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

interface ICheckoutRepository {

    val addresses: SharedFlow<List<Address>>

    suspend fun updateCart()

    suspend fun getCart(): List<LineItem>

    suspend fun makeOrder(orderBody: OrderBody)

    fun getCartItemCount(product: ProductSample): Long

    suspend fun exchangeRate(to: String, from: String, amount: String): Response<CurrencyResponse>

    suspend fun getCustomerAddresses()

    suspend fun createCustomer()

    suspend fun refreshCustomerEphemeralKey()

    suspend fun createPaymentIntent(amount: Double): PaymentIntentApiModel

    suspend fun clearCart()
}