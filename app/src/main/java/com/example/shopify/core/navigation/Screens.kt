package com.example.shopify.core.navigation

sealed class Screens (val route : String){
    object Home: Screens("home")
    object Login : Screens("login_screen")
    object Signup : Screens("signup_screen")

}
