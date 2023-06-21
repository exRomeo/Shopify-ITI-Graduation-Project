package com.example.shopify.presentation.screens.authentication.registeration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

class SignupViewModel(private val authRepository: IAuthRepository) : ViewModel() {

    private val _email: MutableLiveData<String> = MutableLiveData("")
    val email: LiveData<String> = _email
    private val _password: MutableLiveData<String> = MutableLiveData("")
    val password: LiveData<String> = _password

    private var _authResponse: MutableStateFlow<AuthenticationResponseState> =
        MutableStateFlow(AuthenticationResponseState.Loading)
    val authResponse = _authResponse.asStateFlow()
    private val _googleState: MutableStateFlow<GoogleSignInState> =
        MutableStateFlow(GoogleSignInState())
    val googleState get() = _googleState.asStateFlow()

    fun registerUserToShopify(requestBody: CustomerRequestBody) {
        Log.i("TAG", "registerUserToShopify: send body ${requestBody.customer}")
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.registerUserToShopify(requestBody)
            checkShopifyLoggedInState(
                response,
                requestBody.customer.email,
                requestBody.customer.password ?: "A00000#"
            )
        }
    }

    private fun registerUserToFirebase(email: String, password: String, customerId: Long) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val response =
                    authRepository.registerUserToFirebase(email, password, customerId)
                _authResponse.value = response
                Log.i("TAG", "after register to firebase: ${_authResponse.value}")
            }
        }
    }

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = authRepository.googleSignIn(credential)) {
                is AuthenticationResponseState.GoogleSuccess -> {
                    val fullName = response.auth?.user?.displayName
                    val names = fullName?.split(" ")
                    val requestBody =
                        CustomerRequestBody(
                            Customer(
                                firstName = names?.get(0),
                                lastName = names?.get(1),
                                email = response.auth?.user?.email!!,
                                phone = response.auth.user?.phoneNumber,
                                addresses = listOf(Address(address1 = "", phone = response.auth.user?.phoneNumber?:"0xxx")),
                                password = "AB00000ab#",
                                passwordConfirmation = "AB00000ab#",
                                verifiedEmail = response.auth.user?.isEmailVerified
                            )
                        )
                    _googleState.value = GoogleSignInState(success = response.auth)
                    when (val response = authRepository.registerUserToShopify(requestBody)) {
                        is AuthenticationResponseState.Success -> {
                            response.responseBody?.customer?.let { authRepository.addCustomerIDs(it.id) }
                        }

                        is AuthenticationResponseState.Error -> {
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
                    _googleState.value = GoogleSignInState(error = response.message.toString())
                }

                else -> {
                    _googleState.value = GoogleSignInState()
                }

            }
        }

    }

    private fun checkShopifyLoggedInState(
        responseState: AuthenticationResponseState,
        email: String,
        password: String
    ) {

        when (responseState) {

            is AuthenticationResponseState.Loading -> {
                Log.i("TAG", "checkLoggedInState: IS LOADING")
            }

            is AuthenticationResponseState.Success -> {
                Log.i("TAG", "checkLoggedInState: SUCCESS")
                Log.i("TAG", "checkLoggedInState: ${responseState.responseBody}")
                responseState.responseBody?.customer?.let {
                    registerUserToFirebase(
                        email, password,
                        it.id,
                    )
                }
            }

            else -> {
                _authResponse.value = responseState
            }
        }
    }
}