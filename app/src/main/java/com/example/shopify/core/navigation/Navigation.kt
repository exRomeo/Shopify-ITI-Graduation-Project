package com.example.shopify.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopify.core.navigation.settingsnavigation.SettingsNavigation
import com.example.shopify.presentation.screens.authentication.login.LoginScreen
import com.example.shopify.presentation.screens.authentication.registeration.SignupScreen
import com.example.shopify.presentation.screens.homescreen.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Login.route) {
//
        composable(route = Screens.Home.route) {
            HomeScreen(navController)
        }
//        composable(route = Screens.Home.route){
//            HomeScreen(viewModel = viewModel)
//        }

        composable(route = Screens.Login.route) {
               LoginScreen(navController)
        }
        composable(route = Screens.Signup.route) {
            SignupScreen(navController)
        }

        composable(route = Screens.SettingsNavigation.route) {
            SettingsNavigation()
        }
    }
}

@Composable
fun getNavController() = rememberNavController()

@Composable
fun getBackStackEntry(navController: NavHostController) =
    navController.currentBackStackEntryAsState()

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
        route = "settings",
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
        navigationIcon = {
            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Filled.Search, contentDescription = "Navigation icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
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
fun Bottombar() {
    var navController = getNavController()
    val backStackEntry = getBackStackEntry(navController = navController)

    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // navController.navigate(item.route)
                },
                label = {
                    Text(
                        text = item.name,

                        )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                })

        }


    }

}
