package com.example.shopify.presentation.screens.settingsscreen.subscreens.cartscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import com.example.shopify.presentation.composables.CartItemCard
import com.example.shopify.presentation.composables.WarningDialog
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: SettingsViewModel, navController: NavHostController) {
    Scaffold() {

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
    val wishlistItems by viewModel.wishlist.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(wishlistItems) {
            var itemCount by remember { mutableStateOf(it.amount) }
            CartItemCard(product = it, increase = {
                if (itemCount < 10) itemCount++
            }, decrease = {
                if (itemCount > 1) itemCount--
                else showDialog = true
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
            onConfirm = { /*TODO: Implementation of item removal*/ }) {
            showDialog = false
        }
    }
}