package com.example.shopify.core.navigation

sealed class Screens (val route : String){
    object Home: Screens("home")
    object Settings: Screens("settings")
    object Addresses: Screens("addresses")
}
