package com.example.shopify.core.helpers

import com.example.shopify.data.models.ResponseBody
import com.google.firebase.auth.AuthResult

sealed interface AuthenticationResponseState{
    object Loading : AuthenticationResponseState
    class Success(val responseBody : ResponseBody?) : AuthenticationResponseState
    class GoogleSuccess(val auth : AuthResult?) : AuthenticationResponseState
    object NotLoggedIn : AuthenticationResponseState
    class Error(val message : String?): AuthenticationResponseState
}