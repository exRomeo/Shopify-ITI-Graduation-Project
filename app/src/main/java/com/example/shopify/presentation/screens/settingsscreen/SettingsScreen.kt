package com.example.shopify.presentation.screens.settingsscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.user.UserDataRepository
import com.example.shopify.data.repositories.user.remote.UserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.retrofitclient.RetrofitClient
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.common.composables.SettingItemCard

const val TAG = "TAG"


@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, settingsNav: NavHostController) {


    Scaffold() {
        Column(
            modifier = Modifier.padding(it),
        ) {

            val state by settingsViewModel.addresses.collectAsState()
            when (val currentState = state) {
                is UserScreenUISState.Loading -> {
                    LottieAnimation(animation = R.raw.loading_animation)
                }

                is UserScreenUISState.Success<*> -> {
                    val addresses: List<Address> =
                        currentState.data as List<Address>
                    SettingsScreenContent(
                        addresses,
                        settingsViewModel = settingsViewModel,
                        settingsNav = settingsNav
                    )
                }

                is UserScreenUISState.Failure -> {

                }

                else -> {}
            }
        }
    }
}

@Composable
fun SettingsScreenContent(
    addresses: List<Address>,
    settingsViewModel: SettingsViewModel,
    settingsNav: NavHostController
) {


    UserBar(
        imageUrl = "https://louisville.edu/enrollmentmanagement/images/person-icon/image",
        userName = "Please Login",
        email = ""
    )
    SettingsItemList(
        addresses = addresses,
        settingsViewModel = settingsViewModel,
        settingsNav = settingsNav
    )

}


@Composable
fun UserBar(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    userName: String,
    email: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .placeholder(R.drawable.profile_picture_placehodler)
                .error(R.drawable.profile_picture_placehodler)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.profile_picture_placehodler),
            contentDescription = stringResource(R.string.profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .width(65.dp)
                .height(65.dp)
        )
        Spacer(modifier = Modifier.width(32.dp))
        Column {
            Text(
                text = userName,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = email,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            )
        }
    }
    Divider(Modifier.padding(vertical = 8.dp))
}


@Composable
fun SettingsItemList(
    addresses: List<Address>,
    settingsViewModel: SettingsViewModel,
    settingsNav: NavHostController
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
//            val addresses by settingsViewModel.addresses.collectAsState()
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
            SettingItemCard(
                mainText = stringResource(id = R.string.wishlist),
                subText = stringResource(id = R.string.you_have) + " ${wishlist.size} " + stringResource(
                    id = R.string.wishlist_items
                ),
                iconButton = {
                    Icon(Icons.Default.Favorite, stringResource(id = R.string.wishlist))
                }
            ) {
                settingsNav.navigate(Screens.Wishlist.route)
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
                settingsNav.navigate(Screens.Cart.route)
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {
    SettingsScreen(
        SettingsViewModel(UserDataRepository(UserDataRemoteSource(RetrofitClient.customerAddressAPI))),
        rememberNavController()
    )
}











