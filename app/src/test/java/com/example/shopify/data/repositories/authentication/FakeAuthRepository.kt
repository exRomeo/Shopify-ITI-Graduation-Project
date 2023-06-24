package com.example.shopify.data.repositories.authentication

import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.CustomerFirebase
import com.example.shopify.data.models.CustomerRequestBody
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth

class FakeAuthRepository() : IAuthRepository {
    override suspend fun registerUserToShopify(requestBody: CustomerRequestBody): AuthenticationResponseState {
        return AuthenticationResponseState.Success(null)
    }

    override suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerId: Long
    ): AuthenticationResponseState {
        return AuthenticationResponseState.Success(null)
    }

    override suspend fun getSingleCustomerFromShopify(customerID: Long): AuthenticationResponseState {
        return AuthenticationResponseState.Success(null)
    }

    override suspend fun loginUserFirebase(
        email: String,
        password: String
    ): AuthenticationResponseState {
        return AuthenticationResponseState.Success(null)
    }

    override suspend fun signOutFirebase(): Boolean {
        return try {
            FirebaseAuth.getInstance().signOut()
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun googleSignIn(credential: AuthCredential): AuthenticationResponseState {
        TODO("Not yet implemented")
    }

    override fun checkedLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveCustomerIDs(): CustomerFirebase {
        TODO("Not yet implemented")
    }

    override fun addCustomerIDs(customerID: Long) {
        TODO("Not yet implemented")
    }

    override fun updateCustomerID(key: KeyFirebase, newValue: Long) {
        TODO("Not yet implemented")
    }

    override fun saveCustomerIDToSharedPreference(customerID: String) {
        TODO("Not yet implemented")
    }

    override fun readCustomerIDFromSharedPreference(): String? {
        TODO("Not yet implemented")
    }

}