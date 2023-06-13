package com.example.shopify.data.repositories.user.remote.retrofitclient

import com.example.shopify.data.models.ProductResponse
import com.example.shopify.data.models.draftorder.DraftOrderBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DraftOrderAPI {


    @Headers("Content-Type: application/json")
    @POST("admin/api/2023-04/draft_orders.json")
    suspend fun createDraftOrder(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Body draftOrderBody: DraftOrderBody
    ): Response<DraftOrderBody>


    @GET("admin/api/2023-04/draft_orders/{draft_order_id}.json")
    suspend fun getDraftOrder(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("draft_order_id") draftOrderID: Long
    ): Response<DraftOrderBody>


    @Headers("Content-Type: application/json")
    @PUT("admin/api/2023-04/draft_orders/{draft_order_id}.json")
    suspend fun updateDraftOrder(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Body draftOrderBody: DraftOrderBody,
        @Path("draft_order_id") draftOrderID: Long
    ): Response<DraftOrderBody>

@GET("admin/api/2023-04/products/{product_id}.json")
    suspend fun getProductByID(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("product_id") productID: Long
    ): Response<ProductResponse>

}