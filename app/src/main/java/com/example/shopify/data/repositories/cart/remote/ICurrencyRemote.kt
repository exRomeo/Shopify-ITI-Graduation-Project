package com.example.shopify.data.repositories.cart.remote

import com.example.shopify.data.models.currency.CurrencyResponse
import retrofit2.Response

interface ICurrencyRemote {
    suspend fun exchangeRate(to: String, from: String, amount: String): Response<CurrencyResponse>
}