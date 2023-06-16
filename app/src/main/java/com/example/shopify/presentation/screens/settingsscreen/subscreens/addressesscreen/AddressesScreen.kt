package com.example.shopify.presentation.screens.settingsscreen.subscreens.addressesscreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.shopify.R
import com.example.shopify.data.models.address.Address
import com.example.shopify.presentation.common.composables.EditAddressDialog
import com.example.shopify.presentation.common.composables.SettingItemCard
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel


//@Preview(showSystemUi = true)
//@Composable
//fun AddressScreenPreview() {
//
//    val viewModel =
//        SettingsViewModel(
//            UserDataRepository(
//                UserDataRemoteSource(
//                    RetrofitClient.customerAddressAPI,
//                    RetrofitClient.draftOrderAPI
//                )
//            )
//        )
//    AddressScreen(viewModel = viewModel)
//}

@Composable
fun AddressScreen(viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(
                message = it
            )
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
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            val addresses by viewModel.addresses.collectAsState()
            AddressScreenContent(addresses = addresses, viewModel = viewModel)
        }
    }
}

@Composable
fun AddressScreenContent(addresses: List<Address>, viewModel: SettingsViewModel) {
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
    viewModel: SettingsViewModel,
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





