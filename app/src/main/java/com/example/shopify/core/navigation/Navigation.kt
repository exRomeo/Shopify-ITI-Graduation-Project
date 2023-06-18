package com.example.shopify.core.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shopify.core.navigation.settingsnavigation.SettingsNavigation
import com.example.shopify.presentation.screens.authentication.login.LoginScreen
import com.example.shopify.presentation.screens.authentication.registeration.SignupScreen
import com.example.shopify.presentation.screens.brands.BrandsScreen
import com.example.shopify.presentation.screens.categories.CategoriesScreen
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.product_details_screen.ProductDetailsScreen


@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
    ) {
        composable(route = Screens.Home.route) {
            HomeScreenNavigation()
        }
        composable(route = Screens.Login.route) {
            LoginScreen(navController)
        }
        composable(route = Screens.Signup.route) {
            SignupScreen(navController)
        }

    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        name = "Home",
        route = Screens.Home.route,
        icon = Icons.Rounded.Home,
    ),
    BottomNavItem(
        name = "Categories",
        route = "categories",
        icon = Icons.Rounded.List,
    ),
    BottomNavItem(
        name = "Settings",
        route = Screens.Settings.route,
        icon = Icons.Rounded.Settings,
    ),
)

@Composable
fun HomeScreenNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {

        composable(
            route = Screens.Home.route
        ) {
            HomeScreen(navController)
        }

        composable(route = "${Screens.Brands.route}/{collectionId}",
            arguments = listOf(navArgument("collectionId") {
                type = NavType.LongType
            })
        ) {
            BrandsScreen(navController, it.arguments?.getLong("collectionId"))
        }

        composable(route = "${Screens.Details.route}/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.LongType
            })
        ) {
            it.arguments?.getLong("productId")
                ?.let { it1 -> ProductDetailsScreen(navController, it1) }
        }

        composable(
            route = "categories"
        ) {
            CategoriesScreen(navController)
//            Box(
//                contentAlignment = Alignment.Center,
//            ) {
//                Text(text = "Categories")
//            }
        }

        composable(
            route = Screens.Settings.route
        ) {
            SettingsNavigation(bottomNavController = navController)
        }
    }
}


@Composable
fun Bottombar(navController: NavHostController) {

    val backStackEntry by navController.currentBackStackEntryAsState()

    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->
            val selected = item.route == backStackEntry?.destination?.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    Log.i("menna", item.route)
                    if (!selected)
                        navController.navigate(
                            item.route, builder = {
                                launchSingleTop = true
                                popUpTo(Screens.Home.route) {
                                    inclusive = false
                                }
                            }
                        )
                },
                label = {
                    Text(
                        text = item.name
                        )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                }
            )
        }
    }
}



