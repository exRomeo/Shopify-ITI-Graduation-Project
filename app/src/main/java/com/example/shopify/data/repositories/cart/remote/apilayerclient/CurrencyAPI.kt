package com.example.shopify.data.repositories.cart.remote.apilayerclient

import com.example.shopify.data.models.currency.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CurrencyAPI {

    @GET("convert")
    suspend fun exchangeRate(
        @Header("apikey") apikey: String,
        @Query("to") to: String,
        @Query("from") from: String,
        @Query("amount") amount: String
    ): Response<CurrencyResponse>
}