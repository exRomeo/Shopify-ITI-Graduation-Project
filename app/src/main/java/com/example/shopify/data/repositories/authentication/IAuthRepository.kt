package com.example.shopify.data.repositories.authentication

import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.models.RequestBody
import com.google.firebase.auth.AuthCredential

interface IAuthRepository {
    suspend fun registerUserToShopify(requestBody: RequestBody): AuthenticationResponseState
    suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerId: Long
    ): AuthenticationResponseState

    suspend fun loginUserFirebase(email: String, password: String): AuthenticationResponseState
    //suspend fun loginUserWithGoogle(email: String, password: String): AuthenticationResponseState
    suspend fun googleSignIn(credential: AuthCredential) : AuthenticationResponseState
    fun saveCustomerID(customerID:String)
    fun readCustomerID():String?
}