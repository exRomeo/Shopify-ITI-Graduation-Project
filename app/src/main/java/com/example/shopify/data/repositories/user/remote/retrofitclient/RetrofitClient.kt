package com.example.shopify.data.repositories.user.remote.retrofitclient

import android.content.Context
import okhttp3.Cache
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val USER_ID = 7103607996722

object RetrofitClient {
    private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"
    private val retrofit: Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()

    val customerAddressAPI: CustomerAddressAPI =
        retrofit
            .create(CustomerAddressAPI::class.java)
}