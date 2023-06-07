package com.example.shopify.core.navigation.settingsnavigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopify.core.navigation.Screens
import com.example.shopify.presentation.screens.settingsscreen.SettingsScreen
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel

@Composable
fun SettingsNavigation(
    navController: NavHostController = rememberNavController(),
    settingsViewModel: SettingsViewModel = viewModel { SettingsViewModel() }
) {
    NavHost(navController = navController, startDestination = Screens.Settings.route) {
        composable(route = Screens.Settings.route) {
            SettingsScreen(settingsViewModel, navController)
        }
        composable(route = Screens.Settings.route) {
            SettingsScreen(settingsViewModel, navController)
        }
        composable(route = Screens.Settings.route) {
            SettingsScreen(settingsViewModel, navController)
        }
    }
}