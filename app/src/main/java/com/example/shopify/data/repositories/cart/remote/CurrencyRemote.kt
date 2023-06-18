package com.example.shopify.data.repositories.cart.remote

import com.example.shopify.BuildConfig
import com.example.shopify.data.models.currency.CurrencyResponse
import com.example.shopify.data.repositories.cart.remote.apilayerclient.CurrencyAPI
import retrofit2.Response

class CurrencyRemote(private val currencyAPI: CurrencyAPI) : ICurrencyRemote {
    override suspend fun exchangeRate(
        to: String,
        from: String,
        amount: String
    ): Response<CurrencyResponse> =
        currencyAPI.exchangeRate(
            apikey = BuildConfig.CURRENCY_APIKEY,
            to = to,
            from = from,
            amount = amount
        )
}