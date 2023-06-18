package com.example.shopify.data.remote.authentication

import android.util.Log
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.data.models.CustomerFirebase
import com.example.shopify.data.models.CustomerRequestBody
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AuthenticationClient(
    private val authenticationService: IAuthenticationService,
    private val authenticationFirebase: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : IAuthenticationClient {
    override suspend fun getSingleCustomerFromShopify(customerID: Long): AuthenticationResponseState =
        try {
            val response = authenticationService.getSingleCustomerFromShopify(customerID)
            AuthenticationResponseState.Success(response)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }

    override suspend fun registerUserToShopify(customer: CustomerRequestBody): AuthenticationResponseState =
        try {
            val response = authenticationService.registerUserToShopify(customer)
            AuthenticationResponseState.Success(response)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }

    override suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerID: Long
    ): AuthenticationResponseState =
        try {
            authenticationFirebase.createUserWithEmailAndPassword(email, password).await()
            addCustomerIDs(customerID) //throw exception if failure
            if (authenticationFirebase.currentUser == null) {
                AuthenticationResponseState.NotLoggedIn
            } else {
                AuthenticationResponseState.Success(null)
            }
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }


    override suspend fun loginUserFirebase(
        email: String,
        password: String
    ): AuthenticationResponseState =
        try {
            authenticationFirebase.signInWithEmailAndPassword(email, password).await()
            val responseBody = retrieveCustomerIDs()
            Log.i("TAG", "loginUserFirebase: $responseBody")
            if (authenticationFirebase.currentUser == null) {
                AuthenticationResponseState.NotLoggedIn
            } else {
                //updateCustomerID(KeyFirebase.card_id,1523)
                AuthenticationResponseState.Success(null)
            }
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }

    override suspend fun signOutFirebase(): Boolean {
        return try {
            authenticationFirebase.signOut()
            Log.i("TAG", "${authenticationFirebase.currentUser} is signout:")
            true
        } catch (ex: Exception) {
            false
        }
    }

    override suspend fun googleSignIn(credential: AuthCredential): AuthenticationResponseState =
        try {
            val response = authenticationFirebase.signInWithCredential(credential).await()
            AuthenticationResponseState.GoogleSuccess(response)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex)
        }

    override fun checkedLoggedIn(): Boolean =
        authenticationFirebase.currentUser != null


    override fun addCustomerIDs(customerID: Long) {
        val customerMap = hashMapOf(
            "customer_id" to customerID,
            "order_id" to -1,
            "wishlist_id" to -1,
            "card_id" to -1
        )
        val uid = authenticationFirebase.currentUser?.uid ?: ""
        try {
            CoroutineScope(Dispatchers.IO).launch {
                fireStore.collection("customer").document(uid).set(customerMap).await()
            }
        } catch (ex: Exception) {
            Log.i("TAG", "addCustomerID: Failure")
            throw ex
        }
    }

    override fun updateCustomerID(key: KeyFirebase, newValue: Long) {
        val uid = authenticationFirebase.currentUser?.uid ?: ""
        try {
            CoroutineScope(Dispatchers.IO).launch {
                fireStore.collection("customer").document(uid).update(key.toString(), newValue)
                    .await()
            }
        } catch (ex: Exception) {
            Log.i("TAG", "updateCustomerID: Failure")
            throw ex
        }
    }

    override suspend fun retrieveCustomerIDs(): CustomerFirebase = coroutineScope {
        val querySnapshot: DocumentSnapshot?
        val customer: CustomerFirebase?
        return@coroutineScope try {
            val uid = authenticationFirebase.currentUser?.uid ?: ""
            Log.i("TAG", "retrieveCustomerID: $uid")
            querySnapshot = fireStore.collection("customer").document(uid).get().await()
            Log.i("TAG", "retrieveCustomerID: ${querySnapshot.data}")
            val customerData = querySnapshot.toObject<CustomerFirebase>()
            Log.i("TAG", "retrieveCustomerID: $customerData")
            customer = CustomerFirebase(
                customer_id = customerData?.customer_id,
                order_id = customerData?.order_id,
                card_id = customerData?.card_id,
                wishlist_id = customerData?.wishlist_id
            )
            Log.i("TAG", "retrieveCustomer FROM FIREBASE: $customer")
            customer
        } catch (ex: Exception) {
            Log.i("TAG", "retrieveCustomerID: Failure ${ex.message}")
            CustomerFirebase()
        }
    }
}
/*override suspend fun loginUserWithGoogle(
      idToken: String,
      context: Context
  ): AuthenticationResponseState {
      val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestIdToken(idToken)
          .requestEmail()
          .requestProfile()
          .build()
      val signInClient = GoogleSignIn.getClient(context,options)



}*/