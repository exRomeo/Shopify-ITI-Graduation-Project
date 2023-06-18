package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.navigation.Bottombar
import com.example.shopify.core.navigation.Screens
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.repositories.user.UserDataRepository
import com.example.shopify.data.repositories.user.remote.UserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.retrofitclient.ShopifyAPIClient
import com.example.shopify.presentation.common.composables.NoConnectionScreen
import com.example.shopify.presentation.common.composables.SettingItemCard
import com.example.shopify.ui.theme.ShopifyTheme

const val TAG = "TAG"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    bottomNavController: NavHostController,
    settingsNav: NavHostController
) {

    val state by settingsViewModel.settingsState.collectAsState()
    val userName by settingsViewModel.userName.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
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
                            settingsViewModel.logout()
                            bottomNavController.navigate(Screens.Login.route, builder = {
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
                        bottomNavController.navigate(Screens.Login.route, builder = {
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
        bottomBar = { Bottombar(navController = bottomNavController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when (state) {
                is UserScreenUISState.LoggedIn -> {
                    SettingsScreenContent(
                        settingsViewModel = settingsViewModel,
                        bottomNavController = bottomNavController,
                        settingsNav = settingsNav
                    )
                }

                is UserScreenUISState.NotConnected -> {
                    NoConnectionScreen()
                }

                else -> {
                    NotLoggedInSettings(navController = bottomNavController)
                }

            }
        }
    }
}


@Composable
fun SettingsScreenContent(
    settingsViewModel: SettingsViewModel,
    bottomNavController: NavHostController,
    settingsNav: NavHostController
) {

    SettingsItemList(
        settingsViewModel = settingsViewModel,
        bottomNavController = bottomNavController,
        settingsNav = settingsNav
    )
}


@Composable
fun SettingsItemList(
    settingsViewModel: SettingsViewModel,
    bottomNavController: NavHostController,
    settingsNav: NavHostController
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            val addresses by settingsViewModel.addresses.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.addresses),
                subText = stringResource(id = R.string.you_have) + " ${addresses.size} " + stringResource(
                    id = R.string.registered_addresses
                ),
                iconButton = {
                    Icon(Icons.Default.Place, stringResource(id = R.string.addresses))
                }
            ) {
                settingsNav.navigate(Screens.Addresses.route)
            }
        }
        item {
            val wishlist by settingsViewModel.wishlist.collectAsState()
            Log.i(TAG, "SettingsItemList: ${wishlist.size}")
            SettingItemCard(
                mainText = stringResource(id = R.string.wishlist),
                subText = stringResource(id = R.string.you_have) + " ${wishlist.size} " + stringResource(
                    id = R.string.wishlist_items
                ),
                iconButton = {
                    Icon(Icons.Default.Favorite, stringResource(id = R.string.wishlist))
                }
            ) {
                bottomNavController.navigate(Screens.Wishlist.route)
            }
        }
        item {
            val orders by settingsViewModel.orders.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.track_orders),
                subText = stringResource(id = R.string.you_have) + " ${orders.size} " + stringResource(
                    id = R.string.orders
                ),
                iconButton = {
                    Icon(Icons.Default.Info, stringResource(id = R.string.track_orders))
                }
            ) {
                settingsNav.navigate(Screens.Orders.route)
            }
        }
        item {
            val cart by settingsViewModel.cart.collectAsState()
            SettingItemCard(
                mainText = stringResource(id = R.string.cart),
                subText = stringResource(id = R.string.you_have) + " ${cart.size} " + stringResource(
                    id = R.string.cart_items
                ),
                iconButton = {
                    Icon(Icons.Default.ShoppingCart, stringResource(id = R.string.cart))
                }
            ) {
                bottomNavController.navigate(Screens.Cart.route)
            }
        }
    }
}


@Composable
fun NotLoggedInSettings(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.login_request_message), color = Color.Gray)
        TextButton(onClick = {
            navController.navigate(Screens.Login.route, builder = {
                popUpToRoute
            })
        }
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {
    ShopifyTheme() {
        SettingsScreen(
            SettingsViewModel(
                UserDataRepository(
                    UserDataRemoteSource(
                        ShopifyAPIClient.customerAddressAPI
                    )
                ),
                wishlistManager = WishlistManager(
                    ShopifyAPIClient.draftOrderAPI
                ),
                cartManager = CartManager(
                    ShopifyAPIClient.draftOrderAPI
                )
            ),
            rememberNavController(),
            rememberNavController()
        )
    }
}
