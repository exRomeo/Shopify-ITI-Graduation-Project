package com.example.shopify.core.navigation.settingsnavigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.navigation.Screens
import com.example.shopify.presentation.screens.settingsscreen.SettingsScreen
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModelFactory
import com.example.shopify.presentation.screens.settingsscreen.subscreens.addressesscreen.AddressScreen
import com.example.shopify.presentation.screens.settingsscreen.subscreens.cartscreen.CartScreen
import com.example.shopify.presentation.screens.settingsscreen.subscreens.ordersscreen.OrdersScreen
import com.example.shopify.presentation.screens.settingsscreen.subscreens.wishlist.WishlistScreen

@Composable
fun SettingsNavigation(
    settingsNavController: NavHostController = rememberNavController(),
    bottomNavController: NavHostController,
    settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            (LocalContext.current.applicationContext as ShopifyApplication).userDataRepository,
            (LocalContext.current.applicationContext as ShopifyApplication).wishlistManager,
            (LocalContext.current.applicationContext as ShopifyApplication).cartManager
        )
    )
) {
    NavHost(navController = settingsNavController, startDestination = Screens.Settings.route) {
        composable(route = Screens.Settings.route) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                bottomNavController = bottomNavController,
                settingsNav = settingsNavController
            )
        }
        composable(route = Screens.Addresses.route) {
            AddressScreen(settingsViewModel)
        }
        composable(route = Screens.Orders.route) {
            OrdersScreen(settingsViewModel)
        }

        composable(route = Screens.Wishlist.route) {
            WishlistScreen(settingsViewModel)
        }

        composable(route = Screens.Cart.route) {
            CartScreen(viewModel = settingsViewModel, navController = settingsNavController)
        }
    }


}