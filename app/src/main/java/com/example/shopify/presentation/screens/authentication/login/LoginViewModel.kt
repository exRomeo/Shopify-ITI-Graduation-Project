package com.example.shopify.presentation.screens.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.models.GoogleSignInState
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: IAuthRepository) : ViewModel() {

    private var _authResponse: MutableStateFlow<AuthenticationResponseState> =
        MutableStateFlow(AuthenticationResponseState.Loading)
    val authResponse get() = _authResponse.asStateFlow()

    private val _googleState: MutableStateFlow<GoogleSignInState> =
        MutableStateFlow(GoogleSignInState())
    val googleState get() = _googleState.asStateFlow()

    fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val responseState =
                    authRepository.loginUserFirebase(email, password)
                checkLoggedInState(responseState)
            }
        }
    }
    suspend fun logoutUser(): Boolean {
        return authRepository.signOutFirebase()
    }
    fun googleSignIn(credential: AuthCredential) {
        var email : String? = ""
        var password : String? = ""
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = authRepository.googleSignIn(credential)) {
                is AuthenticationResponseState.GoogleSuccess -> {
                    val fullName = response.auth?.user?.displayName
                    val names = fullName?.split(" ")
                    email =response.auth?.user?.email
                    password = "AB00000ab#"
                    val requestBody =
                        CustomerRequestBody(
                            Customer(
                                firstName = names?.get(0),
                                lastName = names?.get(1),
                                email = email!!,
                                phone = password,
                                addresses = listOf(
                                    Address(
                                        address1 = "",
                                        phone = response.auth?.user?.phoneNumber ?: "0xxx"
                                    )
                                ),
                                password = "AB00000ab#",
                                passwordConfirmation = "AB00000ab#",
                                verifiedEmail = response.auth?.user?.isEmailVerified
                            )
                        )
                    _googleState.value = GoogleSignInState(success = response.auth)
                    when (val response = authRepository.registerUserToShopify(requestBody)) {
                        is AuthenticationResponseState.Success -> {
                            response.responseBody?.customer?.let { authRepository.addCustomerIDs(it.id) }
                        }

                        is AuthenticationResponseState.Error -> {
                            //login in firebase

                            _googleState.value =
                                GoogleSignInState(error = response.message.toString())
                        }

                        else -> {
                            Log.i("TAG", "googleSignIn: SUCCESS LOGIN")
                            _googleState.value = GoogleSignInState(loading = true)
                        }
                    }
                }

                is AuthenticationResponseState.Error -> {
                    val responseState =
                        email?.let { password?.let { it1 ->
                            authRepository.loginUserFirebase(it,
                                it1
                            )
                        } }
                    if (responseState != null) {
                        checkLoggedInState(responseState)
                    }
//                    _googleState.value = GoogleSignInState(error = response.message.toString())
                }

                else -> {
                    _googleState.value = GoogleSignInState()
                }

            }
        }

    }

    fun checkedLoggedIn():Boolean{
        return authRepository.checkedLoggedIn()
    }
    private fun checkLoggedInState(responseState: AuthenticationResponseState) {
        when (responseState) {
            is AuthenticationResponseState.Success -> {
                Log.i("TAG", "checkLoggedInState: SUCCESS")
                Log.i(
                    "TAG",
                    "checkLoggedInState: ${responseState.responseBody?.customer?.id}"
                )
                authRepository.saveCustomerIDToSharedPreference(responseState.responseBody?.customer?.id.toString())
                _authResponse.value = responseState
            }

            else -> {
                Log.i(
                    "TAG",
                    "checkLoggedInState: ${responseState}"
                )
                _authResponse.value = responseState
            }
        }
    }
}