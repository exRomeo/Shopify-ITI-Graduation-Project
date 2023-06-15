package com.example.shopify.core.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.shopify.core.navigation.settingsnavigation.SettingsNavigation
import com.example.shopify.presentation.screens.brands.BrandsScreen


@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination ="home"
    ) {
        composable(route = "home") {
       //  HomeScreen(navController = navController, )
        }
        composable(route = "categories") {
            Box(
                modifier = androidx.compose.ui.Modifier.padding(),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = "Categories")
            }
        }

        composable(route = "settings") {
          SettingsNavigation()
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = "${Screens.Brands.route}/{collectionId}"
    ) {


        composable(route = "${Screens.Brands.route}/{collectionId}",
            arguments = listOf(navArgument("collectionId") {
                type = NavType.LongType})){
            BrandsScreen(navController,it.arguments?.getLong("collectionId"), padding = PaddingValues(10.dp))


        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}