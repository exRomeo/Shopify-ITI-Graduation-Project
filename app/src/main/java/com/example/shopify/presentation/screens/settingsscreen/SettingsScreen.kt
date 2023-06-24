package com.example.shopify.presentation.screens.settingsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.navigation.Bottombar
import com.example.shopify.core.navigation.Screens
import com.example.shopify.presentation.common.composables.NoConnectionScreen
import com.example.shopify.presentation.common.composables.NotLoggedInScreen
import com.example.shopify.presentation.common.composables.SettingItemCard
import com.example.shopify.ui.theme.ShopifyTheme
import com.example.shopify.ui.theme.lightMainColor
import com.example.shopify.utilities.ShopifyApplication

const val TAG = "TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            (LocalContext.current.applicationContext as ShopifyApplication).userDataRepository
        )
    )
    val state by viewModel.settingsState.collectAsState()
    val userName by viewModel.userName.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = lightMainColor
                ),
                title = {
                    Text(
                        text = "Welcome $userName",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    if (state is UserScreenUISState.LoggedIn)
                        IconButton(onClick = {
                            viewModel.logout()
                            navController.navigate(Screens.Login.route, builder = {
                                popUpTo(route = Screens.Login.route) {
                                    inclusive = true
                                }
                            }
                            )
                        }
                        ) {
                            Icon(Icons.Default.Logout, stringResource(id = R.string.logout))
                        }
                    else IconButton(onClick = {
                        navController.navigate(Screens.Login.route, builder = {
                            popUpTo(route = Screens.Login.route) {
                                inclusive = true
                            }
                        })
                    }) {
                        Icon(Icons.Default.Login, stringResource(id = R.string.login))
                    }
                }
            )
        },
        bottomBar = { Bottombar(navController = navController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (state) {
                is UserScreenUISState.LoggedIn -> {
                    SettingsScreenContent(
                        viewModel = viewModel,
                        navController = navController
                    )
                }

                is UserScreenUISState.NotConnected -> {
                    NoConnectionScreen()
                }

                else -> {
                    NotLoggedInScreen(navController = navController)
                }

            }
        }
    }
}


@Composable
fun SettingsScreenContent(
    viewModel: SettingsViewModel,
    navController: NavHostController
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            val addresses by viewModel.addresses.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.addresses),
                subText = stringResource(id = R.string.you_have) + " ${addresses.size} " + stringResource(
                    id = R.string.registered_addresses
                ),
                iconButton = {
                    Icon(Icons.Default.Place, stringResource(id = R.string.addresses))
                }
            ) {
                navController.navigate(Screens.Addresses.route)
            }
        }
        item {
            val wishlist by viewModel.wishlist.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.wishlist),
                subText = stringResource(id = R.string.you_have) + " $wishlist " + stringResource(
                    id = R.string.wishlist_items
                ),
                iconButton = {
                    Icon(Icons.Default.Favorite, stringResource(id = R.string.wishlist))
                }
            ) {
                navController.navigate(Screens.Wishlist.route)
            }
        }
        item {
            val orders by viewModel.orders.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.track_orders),
                subText = stringResource(id = R.string.you_have) + " $orders " + stringResource(
                    id = R.string.orders
                ),
                iconButton = {
                    Icon(Icons.Default.Info, stringResource(id = R.string.track_orders))
                }
            ) {
                navController.navigate(Screens.Orders.route)
            }
        }
        item {
            val cart by viewModel.cart.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.cart),
                subText = stringResource(id = R.string.you_have) + " $cart " + stringResource(
                    id = R.string.cart_items
                ),
                iconButton = {
                    Icon(Icons.Default.ShoppingCart, stringResource(id = R.string.cart))
                }
            ) {
                navController.navigate(Screens.Cart.route)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {
    ShopifyTheme() {
        SettingsScreen(
            rememberNavController()
        )
    }
}
