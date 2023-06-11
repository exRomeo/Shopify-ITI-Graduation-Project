package com.example.shopify.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shopify.presentation.screens.authentication.login.LoginScreen
import com.example.shopify.presentation.screens.authentication.login.LoginViewModel
import com.example.shopify.presentation.screens.authentication.registeration.SignupScreen
import com.example.shopify.presentation.screens.authentication.registeration.SignupViewModel
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.homescreen.HomeViewModel

@Composable
fun NavGraph(navController:NavHostController, viewModel: HomeViewModel){
    NavHost(navController = navController, startDestination = Screens.Home.route){
        composable(route = Screens.Home.route){
            HomeScreen(viewModel = viewModel)
        }

        composable(route = Screens.Login.route){
         //   LoginScreen()
        }
        composable(route = Screens.Signup.route){
            //SignupScreen(viewModel as SignupViewModel)
        }
    }
}