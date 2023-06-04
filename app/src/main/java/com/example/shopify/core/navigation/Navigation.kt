package com.example.shopify.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.homescreen.HomeViewModel

@Composable
fun NavGraph(navController:NavHostController, viewModel: HomeViewModel){
    NavHost(navController = navController, startDestination = Screens.Home.route){
        composable(route = Screens.Home.route){
            HomeScreen(viewModel = viewModel)
        }
    }
}