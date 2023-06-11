package com.example.shopify.data.repositories.user.remote.retrofitclient

import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CustomerAddressAPI {

    @GET("admin/api/2023-04/customers/{user_id}/addresses.json")
    suspend fun getAddresses(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("user_id") userID: Long
    ): Response<AddressesResponse>

    @Headers("Content-Type: application/json")
    @POST("admin/api/2023-04/customers/{user_id}/addresses.json")
    suspend fun addAddress(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("user_id") userID: Long,
        @Body address: AddressBody
    ): Response<NewAddressResponse>

    @Headers("Content-Type: application/json")
    @PUT("admin/api/2023-04/customers/{user_id}/addresses/{address_id}.json")
    suspend fun updateAddress(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("user_id") userID: Long,
        @Path("address_id") addressID: Long,
        @Body address: AddressBody
    ): Response<NewAddressResponse>

    @DELETE("admin/api/2023-04/customers/{user_id}/addresses/{address_id}.json")
    suspend fun removeAddress(
        @Header("X-Shopify-Access-Token") accessToken: String,
        @Path("user_id") userID: Long,
        @Path("address_id") addressID: Long
    ): Response<DeleteResponse>

}