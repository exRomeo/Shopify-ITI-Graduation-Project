package com.example.shopify.data.repositories.user.remote.retrofitclient

import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderResponse
import com.example.shopify.data.models.order.OrdersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface OrdersAPI {

    @POST("admin/api/2023-04/orders.json")
    @Headers("Content-Type: application/json")
    suspend fun createOrder(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Body orderBody: OrderBody
    ): Response<OrderResponse>


    @DELETE("admin/api/2023-04/orders/{order_id}.json")
    suspend fun deleteOrder(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("order_id") orderID: Long
    )


    @GET("admin/api/2023-04/orders/{order_id}.json")
    suspend fun getOrder(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("order_id") orderID: Long
    ): Response<OrderResponse>

    @GET("admin/api/2023-04/customers/{customer_id}/orders.json")
    suspend fun getCustomerOrders(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("customer_id") customerID: Long
    ): Response<OrdersResponse>
}