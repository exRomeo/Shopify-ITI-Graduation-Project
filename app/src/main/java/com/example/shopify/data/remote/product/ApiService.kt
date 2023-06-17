package com.example.shopify.data.remote.product

import com.example.shopify.BuildConfig
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN

interface ApiService /*ProductService*/ {
    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/smart_collections.json?")
    suspend fun getBrands(): Response<SmartCollections>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/products.json?")
    suspend fun getRandomProducts(): Response<Products>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/products.json?")
    suspend fun getSpecificBrandProducts(@Query("collection_id") id:Long) : Response<Products>

    @Headers("X-Shopify-Access-Token: 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")
    @GET("/admin/api/2023-04/products.json?")
    suspend fun getProductsBySubcategory(@Query("collection_id") id:Long ,@Query("product_type") type:String) : Response<Products>


    @Headers("X-Shopify-Access-Token: $ACCESS_TOKEN")
    @GET("/admin/api/2023-04/products/{product_id}.json?")
    suspend fun getProductInfo(@Path("product_id") product_ID: Long):
            Response<SingleProductResponseBody>


}