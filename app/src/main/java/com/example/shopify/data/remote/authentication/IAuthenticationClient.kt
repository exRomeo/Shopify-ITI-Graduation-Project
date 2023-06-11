package com.example.shopify.data.remote.authentication

import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.models.RequestBody


private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"

interface IAuthenticationClient {
    suspend fun registerUserToShopify(customer: RequestBody): AuthenticationResponseState

    suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerID: Long
    ): AuthenticationResponseState

    suspend fun loginUserFirebase(email: String, password: String): AuthenticationResponseState


}