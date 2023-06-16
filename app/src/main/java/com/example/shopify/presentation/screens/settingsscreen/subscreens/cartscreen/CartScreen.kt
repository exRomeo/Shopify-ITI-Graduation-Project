package com.example.shopify.presentation.screens.settingsscreen.subscreens.cartscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify.R
import com.example.shopify.data.managers.CartManager
import com.example.shopify.data.managers.WishlistManager
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.user.UserDataRepository
import com.example.shopify.data.repositories.user.remote.UserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.retrofitclient.RetrofitClient
import com.example.shopify.presentation.common.composables.CartItemCard
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel
import com.example.shopify.ui.theme.ShopifyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: SettingsViewModel, navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(context.getString(it))
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                viewModel.addCartItem(
                    ProductSample(
                        id = 8398826111282,
                        title = "",
                        variants = listOf(
                            Product(
                                id = 45344376652082,
                                product_id = 8398826111282,
                                title = "",
                                price = "",
                                availableAmount = 10L
                            )
                        ),
                        image = Image(""),
                        images = listOf(Image(""))
                    )
                )
            }
            ) {
                Text(
                    text = "ADD TEST PRODUCT"
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.cart),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
    ) {
        Column(Modifier.padding(it)) {
            CartScreenContent(
                viewModel = viewModel, navController = navController
            )
        }
    }

}


@Composable
fun CartScreenContent(viewModel: SettingsViewModel, navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    val cartItems by viewModel.cart.collectAsState()
    var itemToRemove by remember { mutableStateOf<ProductSample?>(null) }
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cartItems) {
            var itemCount by remember { mutableStateOf(viewModel.getCartItemCount(it)) }
            CartItemCard(product = it,
                initialCount = itemCount,
                maxCount = it.variants[0].availableAmount!!,
                increase = {
                    if (itemCount < it.variants[0].availableAmount!!) {
                        itemCount++
                    }
                    viewModel.increaseCartItemCount(it)
                }, decrease = {
                    if (itemCount > 1) {
                        itemCount--
                        viewModel.decreaseCartItemCount(it)
                    } else {
                        itemToRemove = it
                        showDialog = true
                    }
                }) {
                /*TODO: Navigation to item detail Page*/
            }
        }
    }

    if (showDialog) {
        WarningDialog(dialogTitle = stringResource(id = R.string.remove_product),
            message = stringResource(id = R.string.cart_item_removal_warning),
            dismissButtonText = stringResource(id = R.string.cancel),
            confirmButtonText = stringResource(id = R.string.remove),
            onConfirm = { itemToRemove?.let { viewModel.removeCart(it) } }) {
            showDialog = false
        }
    }
}

@Preview
@Composable
fun CartPreview() {
    ShopifyTheme() {
        CartScreen(
            viewModel = SettingsViewModel(
                UserDataRepository(
                    UserDataRemoteSource(
                        customerAddressAPI = RetrofitClient.customerAddressAPI,
                        draftOrderAPI = RetrofitClient.draftOrderAPI
                    )
                ),
                cartManager = CartManager(RetrofitClient.draftOrderAPI),
                wishlistManager = WishlistManager(RetrofitClient.draftOrderAPI)
            ), navController = rememberNavController()

        )
    }

}