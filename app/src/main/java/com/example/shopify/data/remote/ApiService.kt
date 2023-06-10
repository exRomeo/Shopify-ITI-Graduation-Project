package com.example.shopify.data.remote

import com.example.shopify.data.models.Brand
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/smart_collections.json?")

    suspend fun getBrands(): Response<List<Brand>>
}