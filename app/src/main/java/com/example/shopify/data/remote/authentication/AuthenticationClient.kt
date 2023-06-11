package com.example.shopify.data.remote.authentication

import android.util.Log
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.models.Errors
import com.example.shopify.data.models.RequestBody
import com.example.shopify.data.models.Response
import com.example.shopify.data.models.ResponseBody
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException

private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"

class AuthenticationClient(
    private val authenticationService: IAuthenticationService,
    private val authenticationFirebase: FirebaseAuth,
    private val fireStore: FirebaseFirestore
) : IAuthenticationClient {

    override suspend fun registerUserToShopify(customer: RequestBody): AuthenticationResponseState {
        return try {
            val response = authenticationService.registerUserToShopify(customer)
            AuthenticationResponseState.Success(response)
        } catch (ex: HttpException) {
            AuthenticationResponseState.Error(ex.toString())
        }
    }

    override suspend fun registerUserToFirebase(
        email: String,
        password: String,
        customerID: Long
    ): AuthenticationResponseState {
        return try {
            authenticationFirebase.createUserWithEmailAndPassword(email, password).await()
            addCustomerID(customerID)
            checkedLoggedIn()
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex.message)
        }
    }

    override suspend fun loginUserFirebase(
        email: String,
        password: String
    ): AuthenticationResponseState {
        return try {
            authenticationFirebase.signInWithEmailAndPassword(email, password).await()
            val responseBody = retrieveCustomerID()
            Log.i("TAG", "loginUserFirebase: $responseBody")
            checkedLoggedIn(responseBody)
        } catch (ex: Exception) {
            AuthenticationResponseState.Error(ex.message)
        }
    }

    private fun checkedLoggedIn(responseBody: ResponseBody? = null): AuthenticationResponseState {
        return if (authenticationFirebase.currentUser == null) {
            AuthenticationResponseState.NotLoggedIn
        } else {
            AuthenticationResponseState.Success(responseBody)
        }
    }

    private fun addCustomerID(customerID: Long) {
        val customerMap = hashMapOf("id" to customerID, "customer_id" to customerID)
        val uid = authenticationFirebase.currentUser?.uid ?: ""
        try {
            CoroutineScope(Dispatchers.IO).launch {
                fireStore.collection("customer").document(uid).set(customerMap).await()
            }
        } catch (ex: Exception) {
            Log.i("TAG", "addCustomerID: Failure")
        }
    }

    private suspend fun retrieveCustomerID(): ResponseBody = coroutineScope {
        val querySnapshot: DocumentSnapshot?
        return@coroutineScope try {
            val uid = authenticationFirebase.currentUser?.uid ?: ""
            Log.i("TAG", "retrieveCustomerID: $uid")
            querySnapshot = fireStore.collection("customer").document(uid).get().await()
            ResponseBody(
                customer = Response(
                    id = 0,
                    customerId = querySnapshot.data?.get("customer_id").toString()
                ),
                errors = null
            )
        } catch (ex: Exception) {
            Log.i("TAG", "retrieveCustomerID: Failure ${ex.message}")
            ResponseBody(
                customer = null,
                errors = Errors(email = listOf(ex.message), phone = null)
            )
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