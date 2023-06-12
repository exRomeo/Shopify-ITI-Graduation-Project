package com.example.shopify.core.helpers

import com.example.shopify.data.remote.authentication.AuthenticationClient
import com.example.shopify.data.remote.authentication.IAuthenticationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private fun getRetrofit(url: String): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getAuthenticationService(url: String):IAuthenticationService{
        return getRetrofit(url).create(IAuthenticationService::class.java)
    }
}