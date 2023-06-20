package com.example.shopify.data.repositories.checkout.remote

import com.example.shopify.BuildConfig
import com.example.shopify.data.models.payment.CustomerApiModel
import com.example.shopify.data.models.payment.EphemeralKeyApiModel
import com.example.shopify.data.models.payment.PaymentIntentApiModel
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

private const val SECRET = BuildConfig.STRIPE_SECRET

interface StripeAPIService {

    @Headers(
        "Authorization: Bearer $SECRET",
        "Stripe-Version: 2022-08-01"
    )
    @POST("v1/customers")
    suspend fun createCustomer(): CustomerApiModel

    @Headers(
        "Authorization: Bearer $SECRET",
        "Stripe-Version: 2022-08-01"
    )
    @POST("v1/ephemeral_keys")
    suspend fun createEphemeralKey(
        @Query("customer") customerId: String
    ): EphemeralKeyApiModel

    @Headers(
        "Authorization: Bearer $SECRET"
    )
    @POST("v1/payment_intents")
    suspend fun createPaymentIntent(
        @Query("customer") customerId: String,
        @Query("amount") amount: Int,
        @Query("currency") currency: String,
        @Query("automatic_payment_methods[enabled]") autoPaymentMethodsEnable: Boolean,
    ): PaymentIntentApiModel
}