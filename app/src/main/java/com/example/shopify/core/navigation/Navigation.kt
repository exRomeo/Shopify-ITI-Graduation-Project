package com.example.shopify.core.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.shopify.presentation.screens.addressesscreen.AddressScreen
import com.example.shopify.presentation.screens.authentication.login.LoginScreen
import com.example.shopify.presentation.screens.authentication.registeration.SignupScreen
import com.example.shopify.presentation.screens.brands.BrandsScreen
import com.example.shopify.presentation.screens.cartscreen.CartScreen
import com.example.shopify.presentation.screens.categories.CategoriesScreen
import com.example.shopify.presentation.screens.checkout.CheckoutScreen
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.onBoarding.OnBoardingScreen
import com.example.shopify.presentation.screens.ordersscreen.OrdersScreen
import com.example.shopify.presentation.screens.product_details_screen.ProductDetailsScreen
import com.example.shopify.presentation.screens.settingsscreen.SettingsScreen
import com.example.shopify.presentation.screens.wishlist.WishlistScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.OnBoarding.route
    ) {

        composable(route = Screens.Login.route) {
            LoginScreen(navController)
        }

        composable(route = Screens.Signup.route) {
            SignupScreen(navController)
        }

        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }


        composable(route = Screens.Addresses.route) {
            AddressScreen(navController = navController)
        }

        composable(
            route = Screens.Categories.route
        ) {
            CategoriesScreen(navController)
        }

        composable(route = Screens.Orders.route) {
            OrdersScreen(navController = navController)
        }

        composable(route = Screens.Cart.route) {
            CartScreen(navController)
        }

        composable(route = Screens.Wishlist.route) {
            WishlistScreen(navController)
        }


        composable(route = Screens.Settings.route) {
            SettingsScreen(navController)
        }

        composable(
            route = "${Screens.Brands.route}/{collectionId}",
            arguments = listOf(navArgument("collectionId") {
                type = NavType.LongType
            })
        ) {
            it.arguments?.getLong("collectionId")?.let { it1 -> BrandsScreen(navController, it1) }
        }

        composable(
            route = "${Screens.Details.route}/{productId}",
            arguments = listOf(navArgument("productId") {
                type = NavType.LongType
            })
        ) {
            it.arguments?.getLong("productId")
                ?.let { productID -> ProductDetailsScreen(navController, productID) }
        }

        composable(route = Screens.OnBoarding.route) {
            OnBoardingScreen(navController)
        }

        composable(route = Screens.Checkout.route) {
            CheckoutScreen(navController)
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
        route = Screens.Categories.route,
        icon = Icons.Rounded.List,
    ),
    BottomNavItem(
        name = "Profile",
        route = Screens.Settings.route,
        icon = Icons.Rounded.Person,
    ),
)


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



