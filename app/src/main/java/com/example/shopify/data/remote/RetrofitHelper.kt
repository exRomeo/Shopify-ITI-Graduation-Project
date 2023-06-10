package com.example.shopify.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

    object RetrofitHelper {
        private const val baseUrl = "https://mad43-alex-and-team2.myshopify.com/"
        private fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

   val apiService : ApiService by lazy {
            getRetrofitInstance().create(ApiService::class.java)
        }

}