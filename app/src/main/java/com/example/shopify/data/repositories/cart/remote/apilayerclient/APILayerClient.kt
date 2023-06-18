package com.example.shopify.data.repositories.cart.remote.apilayerclient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APILayerClient {

    private const val BASE_URL = "https://api.apilayer.com/exchangerates_data/"
    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    val currencyAPI: CurrencyAPI =
        retrofit
            .create(CurrencyAPI::class.java)
}