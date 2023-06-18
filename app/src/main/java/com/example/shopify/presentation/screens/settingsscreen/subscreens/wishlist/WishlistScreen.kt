package com.example.shopify.presentation.screens.settingsscreen.subscreens.wishlist

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.wishlist.WishlistRepository
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.presentation.common.composables.WishlistItemCard
import com.example.shopify.presentation.screens.settingsscreen.TAG


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    navController: NavHostController
) {
    val viewModel: WishlistViewModel = viewModel(
        factory =
        WishlistViewModelFactory(
            WishlistRepository(
                wishlistManager = (LocalContext.current.applicationContext as ShopifyApplication).wishlistManager
            )
        )
    )
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(context.getString(it))
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.addWishlistItem(
                            productID = 8398826111282,
                            variantID = 45344376652082
                        )
                    }) {
                        Icon(Icons.Default.Add, "")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.wishlist),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) {
        Column(Modifier.padding(it)) {
            val state by viewModel.screenState.collectAsState()
            when (state) {
                is UserScreenUISState.Loading -> {
                    LottieAnimation(animation = R.raw.loading_animation)
                }

                is UserScreenUISState.Success<*> -> {
                    WishlistScreenContent(viewModel = viewModel, navController = navController)
                }

                else -> {}
            }
        }
    }
}

@Composable
fun WishlistScreenContent(viewModel: WishlistViewModel, navController: NavHostController) {
    var productToRemove by remember { mutableStateOf<ProductSample?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    val wishlistItems by viewModel.wishlist.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(wishlistItems) {
            Log.i(TAG, "WishlistScreenContent: ${wishlistItems.indexOf(it)}")
            WishlistItemCard(
                product = it,
                onRemoveItem = {
                    productToRemove = it
                    showDialog = true
                }) {
                navController.navigate(Screens.Details.route + "/${it.id}",
                    builder = {
                        launchSingleTop = true
                    }
                )
            }
        }
    }

    if (showDialog) {
        WarningDialog(
            dialogTitle = stringResource(id = R.string.remove_product),
            message = stringResource(id = R.string.wishlist_item_removal_warning),
            dismissButtonText = stringResource(id = R.string.cancel),
            confirmButtonText = stringResource(id = R.string.remove),
            onConfirm = { productToRemove?.let { viewModel.removeWishlistItem(it.id) } }) {
            showDialog = false
        }
    }
}