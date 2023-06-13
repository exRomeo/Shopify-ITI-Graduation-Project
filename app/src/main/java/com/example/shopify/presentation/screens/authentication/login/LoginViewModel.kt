package com.example.shopify.presentation.screens.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.models.GoogleSignInState
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.CoroutineScope
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

    fun googleSignIn(credential: AuthCredential) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.googleSignIn(credential)
            when (response) {
                is AuthenticationResponseState.GoogleSuccess -> {
                    _googleState.value = GoogleSignInState(response.auth)
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