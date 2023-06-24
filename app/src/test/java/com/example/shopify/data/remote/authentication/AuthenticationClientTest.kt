package com.example.shopify.data.remote.authentication

import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.CustomerFirebase
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.models.CustomerResponseBody
import com.example.shopify.data.models.SimpleResponse
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class FakeAuthenticationClientTest(
    private var requestBody: CustomerRequestBody? = null,
    private var responseBody: CustomerResponseBody = CustomerResponseBody(
        SimpleResponse(7132861333810, "nada", "ahmed", "nadaahmed@gmail.com", "+2201245668895")
    )
) : IAuthenticationClient {
    override suspend fun getSingleCustomerFromShopify(customerID: Long): AuthenticationResponseState {
        return try {
            AuthenticationResponseState.Success(responseBody)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }
    }

    override suspend fun registerUserToShopify(customer: CustomerRequestBody): AuthenticationResponseState {
        return try {
            AuthenticationResponseState.Success(responseBody)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }
    }

    override suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerID: Long
    ): AuthenticationResponseState {
        return try {
            AuthenticationResponseState.Success(null)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }
    }

    override suspend fun loginUserFirebase(
        email: String,
        password: String
    ): AuthenticationResponseState {
        return try {
            AuthenticationResponseState.Success(null)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }
    }

    override suspend fun signOutFirebase(): Boolean {
        return try {
            FirebaseAuth.getInstance().signOut()
            true
        } catch (ex: Exception) {
            false
        }
    }


    override fun checkedLoggedIn(): Boolean =
        FirebaseAuth.getInstance().currentUser != null


    override suspend fun retrieveCustomerIDs(): CustomerFirebase {
        return try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val querySnapshot =
                FirebaseFirestore.getInstance().collection("customer").document(uid).get().await()
            val customerData = querySnapshot.toObject<CustomerFirebase>()
            val customer = CustomerFirebase(
                customer_id = customerData?.customer_id,
                order_id = customerData?.order_id,
                card_id = customerData?.card_id,
                wishlist_id = customerData?.wishlist_id
            )
            customer
        } catch (ex: Exception) {
            CustomerFirebase()
        }
    }

    override fun addCustomerIDs(customerID: Long) {
        val customerMap = hashMapOf(
            "customer_id" to customerID,
            "order_id" to -1,
            "wishlist_id" to -1,
            "card_id" to -1
        )
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        try {
            FirebaseFirestore.getInstance().collection("customer").document(uid).set(customerMap)
        } catch (ex: Exception) {
            throw ex
        }
    }

    override fun updateCustomerID(key: KeyFirebase, newValue: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun googleSignIn(credential: AuthCredential): AuthenticationResponseState {
        TODO("Not yet implemented")
    }

}