package com.example.shopify.core.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object SettingsNavigation : Screens("settingsNavigation")
    object Settings : Screens("settings")
    object Addresses : Screens("addresses")
    object Orders : Screens("orders")
    object Wishlist : Screens("wishlist")
    object Cart : Screens("cart")
}
