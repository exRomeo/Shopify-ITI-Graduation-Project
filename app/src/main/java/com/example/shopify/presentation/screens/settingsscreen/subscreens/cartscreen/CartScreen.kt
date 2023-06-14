package com.example.shopify.presentation.screens.settingsscreen.subscreens.cartscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.data.models.ProductSample
import com.example.shopify.presentation.common.composables.CartItemCard
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel

@Composable
fun CartScreen(viewModel: SettingsViewModel, navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(it)
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            BottomAppBar {
                //this was added as a work around to show my floating action button because the BURNED IN bottom bar was hiding it and there was no way to show it "at this moment" unless i added an empty bottom bar :'(

            }
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
                increase = {
                    if (itemCount < 10) {
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
            onConfirm = { viewModel.removeCart(itemToRemove) }) {
            showDialog = false
        }
    }
}