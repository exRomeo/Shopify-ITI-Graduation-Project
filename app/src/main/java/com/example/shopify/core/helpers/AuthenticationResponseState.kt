package com.example.shopify.core.helpers

import com.example.shopify.data.models.ResponseBody

sealed interface AuthenticationResponseState{
    object Loading : AuthenticationResponseState
    class Success(val responseBody : ResponseBody?) : AuthenticationResponseState
    object NotLoggedIn : AuthenticationResponseState
    class Error(val message : String?): AuthenticationResponseState
}