package com.example.shopify.data.repositories.checkout.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PaymentClient {
    private const val baseUrl = "https://api.stripe.com/"
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val stripeAPIService: StripeAPIService = retrofit.create(StripeAPIService::class.java)
}