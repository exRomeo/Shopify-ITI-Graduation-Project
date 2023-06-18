package com.example.shopify.data.repositories.authentication

import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.CustomerFirebase
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.models.CustomerResponseBody
import com.google.firebase.auth.AuthCredential

interface IAuthRepository {
    suspend fun registerUserToShopify(requestBody: CustomerRequestBody): AuthenticationResponseState
    suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerId: Long
    ): AuthenticationResponseState

    suspend fun getSingleCustomerFromShopify(customerID: Long):AuthenticationResponseState
    suspend fun loginUserFirebase(email: String, password: String): AuthenticationResponseState
    //suspend fun loginUserWithGoogle(email: String, password: String): AuthenticationResponseState
    suspend fun signOutFirebase(): Boolean
    suspend fun googleSignIn(credential: AuthCredential) : AuthenticationResponseState
    fun checkedLoggedIn(): Boolean
    suspend fun retrieveCustomerIDs(): CustomerFirebase
    fun addCustomerIDs(customerID: Long)
    fun updateCustomerID(key: KeyFirebase, newValue: Long)
    fun saveCustomerIDToSharedPreference(customerID:String)
    fun readCustomerIDFromSharedPreference():String?
}