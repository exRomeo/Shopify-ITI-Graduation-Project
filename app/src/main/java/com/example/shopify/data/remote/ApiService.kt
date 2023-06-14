package com.example.shopify.data.remote

import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/smart_collections.json?")
    suspend fun getBrands(): Response<SmartCollections>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/products.json?")
    suspend fun getRandomProducts(): Response<Products>
//val id:Long
    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/products.json?")
    suspend fun getSpecificBrandProducts(@Query("collection_id") id:Long) : Response<Products>
}