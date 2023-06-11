package com.example.shopify.data.remote.authentication

import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.RequestBody
import com.example.shopify.data.models.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private const val API_KEY = "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"
interface IAuthenticationService {
    @POST("admin/api/2023-04/customers.json")
    @Headers("X-Shopify-Access-Token:$API_KEY","Content-Type: application/json")
    suspend fun registerUserToShopify(@Body customer: RequestBody): ResponseBody

    @GET("admin/api/2023-04/customers/7110233489714.json")
    @Headers("X-Shopify-Access-Token:$API_KEY")
    suspend fun loginUserToShopify(@Body customer: Customer):ResponseBody

}