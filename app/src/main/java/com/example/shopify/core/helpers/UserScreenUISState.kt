package com.example.shopify.core.helpers

sealed class UserScreenUISState {

    object Loading : UserScreenUISState()

    object NotLoggedIn : UserScreenUISState()

    class Success<UserData>(val data: UserData) : UserScreenUISState()

    class Failure(val message: String) : UserScreenUISState()
}
