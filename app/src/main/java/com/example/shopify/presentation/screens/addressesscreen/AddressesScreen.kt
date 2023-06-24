package com.example.shopify.presentation.screens.addressesscreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.address.AddressRepository
import com.example.shopify.presentation.common.composables.EditAddressDialog
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.common.composables.NoConnectionScreen
import com.example.shopify.presentation.common.composables.NotLoggedInScreen
import com.example.shopify.presentation.common.composables.SettingItemCard
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.ui.theme.lightMainColor
import com.example.shopify.utilities.ShopifyApplication


@Preview(showSystemUi = true)
@Composable
fun AddressScreenPreview() {
    AddressScreen(rememberNavController())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(navController: NavHostController) {

    val viewModel: AddressesViewModel = viewModel(
        factory = AddressesViewModelFactory(
            addressRepository = AddressRepository(
                addressManager =
                (LocalContext.current.applicationContext as ShopifyApplication)
                    .addressManager
            )
        )
    )
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(context.getString(it))
        }

    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_address)
                )
                if (showDialog) {
                    EditAddressDialog(
                        dialogTitle = stringResource(id = R.string.add_address),
                        dismissButtonText = stringResource(id = R.string.cancel),
                        confirmButtonText = stringResource(id = R.string.save),
                        onConfirm = { newAddress ->
                            viewModel.addAddress(newAddress)
                        },
                        onDismiss = {
                            showDialog = false
                        }
                    )
                }

            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = lightMainColor
                ),
                title = {
                    Text(
                        text = stringResource(id = R.string.addresses),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val state by viewModel.screenState.collectAsState()
            when (state) {

                is UserScreenUISState.Loading -> {
                    LottieAnimation(animation = R.raw.loading_animation)
                }

                is UserScreenUISState.Success<*> -> {
                    val addresses = (state as UserScreenUISState.Success<*>).data as List<Address>
                    AddressScreenContent(addresses = addresses, viewModel = viewModel)
                }

                is UserScreenUISState.NotConnected -> {
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
fun AddressScreenContent(addresses: List<Address>, viewModel: AddressesViewModel) {
    var addressToEdit: Address? by remember { mutableStateOf(null) }

    var showEditDialog by remember { mutableStateOf(false) }

    AddressList(addresses = addresses, viewModel = viewModel) {
        addressToEdit = it
        showEditDialog = true
    }
    if (showEditDialog) {
        addressToEdit?.let {
            EditAddressDialog(
                dialogTitle = stringResource(id = R.string.edit_address),
                address = it,
                dismissButtonText = stringResource(id = R.string.cancel),
                confirmButtonText = stringResource(id = R.string.save),
                onConfirm = { newAddress ->
                    viewModel.updateAddress(newAddress)
                },
                onDismiss = {
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun AddressList(
    modifier: Modifier = Modifier,
    addresses: List<Address>,
    viewModel: AddressesViewModel,
    onClick: (Address) -> Unit
) {
    var showRemoveConfirmationDialog by remember { mutableStateOf(false) }
    var addressToRemove: Address? by remember { mutableStateOf(null) }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = addresses, key = { it.id }) {
            SettingItemCard(
                mainText = it.getAddressString(),
                subText = it.phoneNumber,
                onClick = {
                    onClick(it)
                },
                iconButton = {
                    IconButton(
                        enabled = !it.default,
                        onClick = {
                            addressToRemove = it
                            showRemoveConfirmationDialog = true
                        }
                    ) {
                        Icon(
                            if (it.default) Icons.Default.Home else Icons.Default.Delete,
                            contentDescription = stringResource(id = R.string.remove_address)
                        )
                    }
                }
            )
        }
    }

    if (showRemoveConfirmationDialog) {
        WarningDialog(
            dialogTitle = stringResource(id = R.string.remove_address),
            message = stringResource(id = R.string.addr_removal_warning),
            dismissButtonText = stringResource(id = R.string.cancel),
            confirmButtonText = stringResource(id = R.string.remove),
            onConfirm = { addressToRemove?.let { viewModel.removeAddress(it) } }) {
            showRemoveConfirmationDialog = false
        }
    }
}





