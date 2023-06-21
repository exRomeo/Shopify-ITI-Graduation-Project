package com.example.shopify.core.helpers

sealed class UserScreenUISState {

    object Loading : UserScreenUISState()

    object NotLoggedIn : UserScreenUISState()
    object LoggedIn : UserScreenUISState()

    object NotConnected : UserScreenUISState()

    class Success<UserData>(val data: UserData) : UserScreenUISState()

    object NoData : UserScreenUISState()

    class Failure(val message: String) : UserScreenUISState()

}
