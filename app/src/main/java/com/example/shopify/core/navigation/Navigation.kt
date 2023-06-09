package com.example.shopify.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopify.core.navigation.settingsnavigation.SettingsNavigation
import com.example.shopify.presentation.screens.homescreen.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Home.route) {

        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }

        composable(route = Screens.SettingsNavigation.route) {
            SettingsNavigation()
        }

    }
}


