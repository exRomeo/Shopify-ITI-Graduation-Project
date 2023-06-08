package com.example.shopify.core.navigation

sealed class Screens (val route : String){
    object Home: Screens("home")
    object Categories: Screens("categories")
    object Settings: Screens("settings")
}
