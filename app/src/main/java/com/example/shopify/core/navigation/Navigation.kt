package com.example.shopify.core.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

import com.example.shopify.presentation.screens.homescreen.HomeScreen


@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController,
        startDestination = Screens.Login.route) {
        composable(route = Screens.Home.route) {

          // HomeScreen(navController)
            Test()

        }
        composable(route = Screens.Login.route) {

            LoginScreen(navController)
            //  LoginScreen()

        }
        composable(route = Screens.Signup.route) {
            SignupScreen(navController)
        }
//        composable(route = "${Screens.Brands.route}/{collectionId}",
//            arguments = listOf(navArgument("collectionId") {
//                type = NavType.LongType})){
//            BrandsScreen(navController, it.arguments?.getLong("collectionId"), padding)
//
//
//            }
            //SignupScreen(viewModel as SignupViewModel)
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
        navigationIcon = {
          //  SearchBar(query = "", onQueryChange ={} , onSearch ={} , active = true, onActiveChange = {}) {

          //  }
//            IconButton(onClick = onSearch) {
//                Icon(
//                    imageVector = Icons.Filled.Search, contentDescription = "Navigation icon",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }
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


//@Composable
//private fun currentRoute(navController: NavHostController): String? {
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    return navBackStackEntry?.arguments?.getString("Settings")
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Test() {
    val navController = rememberNavController()

    Scaffold(

        topBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val route  = backStackEntry?.destination?.route?.split("/")
            route?.get(0)?.let { TopBar(title = it  ,onSearch = {}) }
        },
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()

            NavigationBar(containerColor = Color.White) {
                bottomNavItems.forEach { item ->
                    val selected = item.route == backStackEntry?.destination?.route
                    //  val currentRoute = currentRoute(navController)

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            //  if (currentRoute != item.route) {
                            Log.i("menna", item.route)
                            navController.navigate(item.route)
                            //  }
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
    ) { padding ->
        NavHost(navController = navController, startDestination = "home") {
            composable(
                route = Screens.Home.route
            ) {
                HomeScreen(navController,padding)

            }

            composable(route = "${Screens.Brands.route}/{collectionId}",
                arguments = listOf(navArgument("collectionId") {
                    type = NavType.LongType})){
                BrandsScreen(navController,it.arguments?.getLong("collectionId"),padding)


            }


            composable(
                route = "categories"
            ) {
                Box(
                    modifier = Modifier.padding(padding),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Categories")
                }
            }

            composable(
                route = Screens.Settings.route
            ) {
                SettingsNavigation()
            }
        }
    }
}

@Composable
fun Bottombar(navController: NavHostController) {
    //navController.currentBackStackEntryAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
   // return navBackStackEntry?.arguments?.getString("Settings")
    //val backStackEntry = getBackStackEntry(navController = navController)
    NavigationBar(containerColor = Color.White) {
        bottomNavItems.forEach { item ->
            val selected = item.route == backStackEntry?.destination?.route
            //  val currentRoute = currentRoute(navController)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    //  if (currentRoute != item.route) {
                    Log.i("menna", item.route)
                    navController.navigate(item.route)
                    //  }
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


    }}



