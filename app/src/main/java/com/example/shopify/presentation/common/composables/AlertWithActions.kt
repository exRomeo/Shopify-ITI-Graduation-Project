package com.example.shopify.presentation.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import com.example.shopify.data.models.address.Address

@Composable
fun EditAddressDialog(
    dialogTitle: String,
    address: Address = Address(phoneNumber = "", firstName = "", lastName = "", fullAddress = ""),
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: (newAddress: Address) -> Unit,
    onDismiss: () -> Unit
) {
    var phoneNumber by remember { mutableStateOf(address.phoneNumber) }
    var firstName by remember { mutableStateOf(address.firstName) }
    var lastName by remember { mutableStateOf(address.lastName) }
    var fullAddress by remember { mutableStateOf(address.fullAddress) }
    var default by remember { mutableStateOf(address.default) }

    fun clearFields() {
        phoneNumber = ""
        firstName = ""
        lastName = ""
        fullAddress = ""
    }

    val pattern = Regex("^[a-zA-Z ]+$")

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
                    placeholder = { Text(text = "ex +20 1122345678") },
                    isError = phoneNumber.isEmpty(),
                    label = { Text(text = stringResource(id = R.string.phone_number)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

                )

                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(pattern)) {
                            firstName = it
                        }
                    },
                    isError = firstName.isEmpty(),
                    label = { Text(text = stringResource(id = R.string.first_name)) }
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        if (it.isEmpty() || it.matches(pattern)) {
                            lastName = it
                        }
                    },
                    isError = lastName.isEmpty(),
                    label = { Text(text = stringResource(id = R.string.last_name)) }
                )

                OutlinedTextField(
                    value = fullAddress,
                    onValueChange = { fullAddress = it },
                    isError = fullAddress.isEmpty(),
                    label = { Text(text = stringResource(id = R.string.full_address)) },
                    maxLines = 2
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.default_address),
                        style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize)
                    )
                    Checkbox(checked = default, onCheckedChange = { default = it })
                }


            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (phoneNumber.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && fullAddress.isNotEmpty()) {
                        onConfirm(
                            Address(
                                id = address.id,
                                customerID = address.customerID,
                                phoneNumber = phoneNumber,
                                firstName = firstName,
                                lastName = lastName,
                                fullAddress = fullAddress,
                                default = default
                            )
                        )
                        onDismiss()
                        clearFields()
                    }
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
fun WarningDialog(
    dialogTitle: String,
    message: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    addDismissButton :Boolean = true,
    onDismiss: () -> Unit,

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
            if (addDismissButton) {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = dismissButtonText)
                }
            }
        }
    )
}

@Composable
@Preview
fun RemoveConfirmationDialogPreview() {
    WarningDialog(
        dialogTitle = "Remove Address",
        message = "Are you sure you want to remove this address ??",
        dismissButtonText = "Cancel",
        confirmButtonText = "Remove",
        onConfirm = {},
        onDismiss = {}
    )
}