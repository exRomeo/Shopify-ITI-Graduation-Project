package com.example.shopify.data.remote.authentication

import com.example.shopify.BuildConfig
import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.models.CustomerResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

private const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN

interface IAuthenticationService {

    @GET("admin/api/2023-04/customers/{customer_id}.json")
    @Headers("X-Shopify-Access-Token:$ACCESS_TOKEN")
    suspend fun getSingleCustomerFromShopify(@Path("customer_id") Customer_Id: Long): CustomerResponseBody

    @POST("admin/api/2023-04/customers.json")
    @Headers("X-Shopify-Access-Token:$ACCESS_TOKEN", "Content-Type: application/json")
    suspend fun registerUserToShopify(@Body customer: CustomerRequestBody): CustomerResponseBody

    @GET("admin/api/2023-04/customers/7110233489714.json")
    @Headers("X-Shopify-Access-Token:$ACCESS_TOKEN")
    suspend fun loginUserToShopify(@Body customer: Customer): CustomerResponseBody

}