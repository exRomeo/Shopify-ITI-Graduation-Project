package com.example.shopify.core.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Categories : Screens("categories")
    object Settings : Screens("settings")
    object Login : Screens("login_screen")
    object Signup : Screens("signup_screen")
    object Addresses : Screens("addresses")
    object Orders : Screens("orders")
    object Wishlist : Screens("wishlist")
    object Cart : Screens("cart")
    object Brands : Screens("brands")
    object Details : Screens("details")
    object OnBoarding : Screens("onBoarding")
    object Checkout : Screens("checkout")
}
