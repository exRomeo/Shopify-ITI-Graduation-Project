package com.example.shopify.presentation.screens.ordersscreen

import android.util.Log
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
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.order.OrderIn
import com.example.shopify.data.repositories.orders.OrdersRepository
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.common.composables.NoConnectionScreen
import com.example.shopify.presentation.common.composables.NoData
import com.example.shopify.presentation.common.composables.NotLoggedInScreen
import com.example.shopify.presentation.common.composables.OrderItemCard
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.presentation.screens.settingsscreen.TAG
import com.example.shopify.utilities.ShopifyApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavHostController) {
    val viewModel: OrdersViewModel = viewModel(
        factory = OrdersViewModelFactory(
            ordersRepository = OrdersRepository(
                ordersManager =
                (LocalContext.current.applicationContext as ShopifyApplication).ordersManager
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.track_orders),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) {

        Column(Modifier.padding(it)) {
            val state by viewModel.state.collectAsState()
            when (state) {
                is UserScreenUISState.Loading -> {
                    Log.i(TAG, "OrdersScreen: LOADING")
                    LottieAnimation(animation = R.raw.loading_animation)
                }

                is UserScreenUISState.Success<*> -> {
                    val orders = (state as UserScreenUISState.Success<*>).data as List<OrderIn>
                    Log.i(TAG, "OrdersScreen: SUCESS")
                    OrdersScreenContent(viewModel = viewModel, orders = orders)
                }

                is UserScreenUISState.NoData -> {
                    Log.i(TAG, "OrdersScreen: NO DADA")
                    NoData(message = "Make Some Orders!")
                }

                is UserScreenUISState.NotConnected -> {
                    Log.i(TAG, "OrdersScreen: NOD GONEECdED")
                    NoConnectionScreen()
                }

                is UserScreenUISState.NotLoggedIn -> {
                    NotLoggedInScreen(navController = navController)
                }

                else -> {}
            }
        }
    }
}

@Composable
fun OrdersScreenContent(viewModel: OrdersViewModel, orders: List<OrderIn>) {
    var showDialog by remember { mutableStateOf(false) }
    var orderToCancel by remember { mutableStateOf<OrderIn?>(null) }
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(orders) {
            OrderItemCard(
                order = it,
                onCancelClick = {
                    orderToCancel = it
                    showDialog = true
                }
            ) {
                Log.i(TAG, "OrdersScreenContent: ${it.orderURL}")
            }
        }
    }

    if (showDialog) {
        WarningDialog(
            dialogTitle = stringResource(id = R.string.cancel_order),
            message = stringResource(id = R.string.order_cancellation_warning),
            dismissButtonText = stringResource(id = R.string.no),
            confirmButtonText = stringResource(id = R.string.yes),
            onConfirm = { orderToCancel?.let { viewModel.cancelOrder(it.id) } }) {
            showDialog = false
        }
    }
}