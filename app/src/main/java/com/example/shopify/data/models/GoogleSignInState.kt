package com.example.shopify.data.models

import com.google.firebase.auth.AuthResult

data class GoogleSignInState(
    val success : AuthResult? = null,
    val error : String = ""
)
