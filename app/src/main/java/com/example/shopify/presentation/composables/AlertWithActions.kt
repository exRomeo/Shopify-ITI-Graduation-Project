package com.example.shopify.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import com.example.shopify.data.models.Address
import com.example.shopify.presentation.screens.settingsscreen.TAG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressDialog(
    dialogTitle: String,
    address: Address = Address(0, "", "", "", "", ""),
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: (newAddress: Address) -> Unit,
    onDismiss: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf(address.phoneNumber) }
    var streetName by remember { mutableStateOf(address.streetName) }
    var buildingNumber by remember { mutableStateOf(address.buildingNumber) }
    var city by remember { mutableStateOf(address.city) }
    var zipCode by remember { mutableStateOf(address.zipCode) }

    fun clearFields() {
        phoneNumber = ""
        streetName = ""
        buildingNumber = ""
        city = ""
        zipCode = ""
    }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
            clearFields()
        },
        title = {
            Text(
                text = dialogTitle,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(Modifier.padding(4.dp)) {

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = { Text(text = "ex +20 01122458") },
                    label = { Text(text = stringResource(id = R.string.phone_number)) })

                OutlinedTextField(
                    value = buildingNumber,
                    onValueChange = { buildingNumber = it },
                    label = { Text(text = stringResource(id = R.string.building_number)) })

                OutlinedTextField(
                    value = streetName,
                    onValueChange = { streetName = it },
                    label = { Text(text = stringResource(id = R.string.street_name)) })

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text(text = stringResource(id = R.string.city)) })

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text(text = stringResource(id = R.string.zip_code)) }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        Address(
                            address.id,
                            phoneNumber,
                            buildingNumber,
                            streetName,
                            city,
                            zipCode
                        )
                    )
                    Log.i(TAG, "EditAddressDialog: (${address.id})")
                    onDismiss()
                    clearFields()
                }
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    clearFields()
                }
            ) {
                Text(text = dismissButtonText)
            }
        }
    )
}


@Composable
@Preview
fun EditAddressDialogPreview() {
    EditAddressDialog(
        dialogTitle = "Edit Address",
        dismissButtonText = "Cancel",
        confirmButtonText = "Save",
        onConfirm = {}
    ) {

    }
}

@Composable
fun RemoveConfirmationDialog(
    dialogTitle: String,
    message: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(
                text = dialogTitle,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            )

        },
        text = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = message,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                onDismiss()
            }, colors = ButtonDefaults.buttonColors(Color.Red)) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()

                }
            ) {
                Text(text = dismissButtonText)
            }
        }
    )
}

@Composable
@Preview
fun RemoveConfirmationDialogPreview() {
    RemoveConfirmationDialog(
        dialogTitle = "Remove Address",
        message = "Are you sure you want to remove this address ??",
        dismissButtonText = "Cancel",
        confirmButtonText = "Remove",
        onConfirm = {},
        onDismiss = {}
    )
}