package com.example.shopify.presentation.screens.settingsscreen.subscreens.addressesscreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import com.example.shopify.data.models.Address
import com.example.shopify.presentation.composables.EditAddressDialog
import com.example.shopify.presentation.composables.RemoveConfirmationDialog
import com.example.shopify.presentation.composables.SettingItemCard
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel


@Preview(showSystemUi = true)
@Composable
fun AddressScreenPreview() {

    val viewModel = SettingsViewModel()

    AddressScreen(viewModel = viewModel)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(viewModel: SettingsViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { showDialog = true }) {
            Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_address))
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
    }) {
        Column(modifier = Modifier.padding(it)) {
            AddressScreenContent(viewModel = viewModel)
        }
    }
}

@Composable
fun AddressScreenContent(viewModel: SettingsViewModel) {
    var addressToEdit: Address? by remember { mutableStateOf(null) }

    var showEditDialog by remember { mutableStateOf(false) }

    AddressList(viewModel = viewModel) {
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
    viewModel: SettingsViewModel,
    onClick: (Address) -> Unit
) {
    val addresses by viewModel.addresses.collectAsState()
    var showRemoveConfirmationDialog by remember { mutableStateOf(false) }
    var addressToRemove: Address? by remember { mutableStateOf(null) }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = addresses, key = { it.id }) {

            Box() {
                SettingItemCard(
                    mainText = it.getAddressString(),
                    subText = it.phoneNumber,
                    onClick = {
                        onClick(it)
                    }
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {
                        addressToRemove = it
                        showRemoveConfirmationDialog = true
                    }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(id = R.string.remove_address)
                    )
                }
            }
        }
    }

    if (showRemoveConfirmationDialog) {
        RemoveConfirmationDialog(
            dialogTitle = stringResource(id = R.string.remove_address),
            message = stringResource(id = R.string.addr_removal_warning),
            dismissButtonText = stringResource(id = R.string.cancel),
            confirmButtonText = stringResource(id = R.string.remove),
            onConfirm = { addressToRemove?.let { viewModel.removeAddress(it) } }) {
            showRemoveConfirmationDialog = false
        }
    }
}





