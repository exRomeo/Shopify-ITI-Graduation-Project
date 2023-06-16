package com.example.shopify.presentation.screens.settingsscreen.subscreens.ordersscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.shopify.R
import com.example.shopify.presentation.common.composables.OrderItemCard
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel

@Composable
fun OrdersScreen(viewModel: SettingsViewModel) {
    Scaffold() {

        Column(Modifier.padding(it)) {
            OrdersScreenContent(viewModel = viewModel)
        }
    }
}

@Composable
fun OrdersScreenContent(viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val orders by viewModel.orders.collectAsState()
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(orders) {
            OrderItemCard(
                product = it,
                onCancelClick = { showDialog = true }
            ) {
                /*TODO: Navigation to item detail Page*/
            }
        }
    }

    if (showDialog) {
        WarningDialog(
            dialogTitle = stringResource(id = R.string.cancel_order),
            message = stringResource(id = R.string.order_cancellation_warning),
            dismissButtonText = stringResource(id = R.string.no),
            confirmButtonText = stringResource(id = R.string.yes),
            onConfirm = { /*TODO: Remove order logic here*/ }) {
            showDialog = false
        }
    }
}