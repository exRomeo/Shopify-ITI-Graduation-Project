package com.example.shopify.presentation.screens.authentication.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.repositories.authentication.IAuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: IAuthRepository) : ViewModel() {

    private var _authResponse: MutableStateFlow<AuthenticationResponseState> =
        MutableStateFlow(AuthenticationResponseState.Loading)
    val authResponse get() = _authResponse.asStateFlow()

    fun loginUser(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val responseState =
                    authRepository.loginUserFirebase(email, password)
                checkLoggedInState(responseState)
            }
        }
    }

    private fun checkLoggedInState(responseState: AuthenticationResponseState) {
        when (responseState) {
            is AuthenticationResponseState.Success -> {
                Log.i("TAG", "checkLoggedInState: SUCCESS")
                Log.i(
                    "TAG",
                    "checkLoggedInState: ${responseState.responseBody?.customer?.customerId}"
                )
                responseState.responseBody?.customer?.customerId?.let {
                    authRepository.saveCustomerID(
                        it
                    )
                }
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