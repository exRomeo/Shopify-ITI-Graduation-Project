package com.example.shopify.data.repositories.authentication

import android.content.SharedPreferences
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.core.utils.SharedPreference.customerID
import com.example.shopify.data.models.CustomerFirebase
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.models.CustomerResponseBody
import com.example.shopify.data.remote.authentication.IAuthenticationClient
import com.google.firebase.auth.AuthCredential


class AuthRepository(
    private val authenticationClient: IAuthenticationClient,
    private val sharedPreference: SharedPreferences
) : IAuthRepository {
    override suspend fun getSingleCustomerFromShopify(customerID: Long): AuthenticationResponseState =
        authenticationClient.getSingleCustomerFromShopify(customerID)

    override suspend fun registerUserToShopify(requestBody: CustomerRequestBody): AuthenticationResponseState =
        authenticationClient.registerUserToShopify(requestBody)


    override suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerId: Long
    ): AuthenticationResponseState =
        authenticationClient.registerUserToFirebase(email, password, customerId)

    override suspend fun loginUserFirebase(
        email: String,
        password: String
    ): AuthenticationResponseState = authenticationClient.loginUserFirebase(email, password)

    override suspend fun signOutFirebase(): Boolean = authenticationClient.signOutFirebase()


    override suspend fun googleSignIn(credential: AuthCredential): AuthenticationResponseState =
        authenticationClient.googleSignIn(credential)

    override fun checkedLoggedIn(): Boolean =
        authenticationClient.checkedLoggedIn()


    override suspend fun retrieveCustomerIDs(): CustomerFirebase =
        authenticationClient.retrieveCustomerIDs()


    override fun addCustomerIDs(customerID: Long) =
        authenticationClient.addCustomerIDs(customerID)

    override fun updateCustomerID(key: KeyFirebase, newValue: Long) =
        authenticationClient.updateCustomerID(key, newValue)

    override fun saveCustomerIDToSharedPreference(customerID: String) {
        sharedPreference.customerID = customerID
    }

    override fun readCustomerIDFromSharedPreference(): String? =
        sharedPreference.customerID
}