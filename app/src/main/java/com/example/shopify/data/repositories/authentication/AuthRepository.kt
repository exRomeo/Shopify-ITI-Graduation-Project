package com.example.shopify.data.repositories.authentication

import android.content.SharedPreferences
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.utils.SharedPreference
import com.example.shopify.core.utils.SharedPreference.customerID
import com.example.shopify.data.models.RequestBody
import com.example.shopify.data.remote.authentication.IAuthenticationClient
import com.example.shopify.data.remote.authentication.IAuthenticationService


class AuthRepository(
    private val authenticationClient: IAuthenticationClient,
    private val sharedPreference: SharedPreferences
) : IAuthRepository {
    override suspend fun registerUserToShopify(requestBody: RequestBody): AuthenticationResponseState {
        return authenticationClient.registerUserToShopify(requestBody)
    }

    override suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerId: Long
    ): AuthenticationResponseState {
        return authenticationClient.registerUserToFirebase(email, password, customerId)
    }

    override suspend fun loginUserFirebase(
        email: String,
        password: String
    ): AuthenticationResponseState {
        return authenticationClient.loginUserFirebase(email, password)
    }

    override fun saveCustomerID(customerID: String) {
        sharedPreference.customerID = customerID
    }

    override fun readCustomerID(): String? {
        return sharedPreference.customerID
    }
}