package com.example.shopify.core.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.shopify.core.navigation.settingsnavigation.SettingsNavigation
import com.example.shopify.presentation.screens.authentication.login.LoginScreen
import com.example.shopify.presentation.screens.authentication.registeration.SignupScreen
import com.example.shopify.presentation.screens.brands.BrandsScreen
import com.example.shopify.presentation.screens.cartscreen.CartScreen
import com.example.shopify.presentation.screens.categories.CategoriesScreen
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.product_details_screen.ProductDetailsScreen
import com.example.shopify.presentation.screens.wishlist.WishlistScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route
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

        composable(
            route = Screens.Categories.route
        ) {
            CategoriesScreen(navController)
        }

        composable(route = Screens.Cart.route) {
            CartScreen(navController)
        }

        composable(route = Screens.Wishlist.route) {
            WishlistScreen(navController)
        }


        composable(route = Screens.Settings.route) {
            SettingsNavigation(bottomNavController = navController)
        }

        composable(
            route = "${Screens.Brands.route}/{collectionId}",
            arguments = listOf(navArgument("collectionId") {
                type = NavType.LongType
            })
        ) {
            BrandsScreen(navController, it.arguments?.getLong("collectionId"))
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier, title: String, onSearch: () -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint
        ),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary
            )


        },
//        navigationIcon = {
//            IconButton(onClick = onSearch) {
//                Icon(
//                    imageVector = Icons.Filled.Search, contentDescription = "Navigation icon",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//        },
        actions = {
            //navigate to carts
            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart, contentDescription = "shopping cart",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            //navigate to favourites
            IconButton(onClick = {
            }) {
                Icon(
                    imageVector = Icons.Outlined.Favorite, contentDescription = "Favourite",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    )
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



