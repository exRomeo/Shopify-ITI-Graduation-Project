package com.example.shopify.presentation.screens.settingsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R
import com.example.shopify.data.models.Address

const val TAG = "TAG"

@Composable
fun UserBar(modifier: Modifier = Modifier, imageUrl: String = "", userName: String, email: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.profile_picture_placehodler),
            contentDescription = stringResource(R.string.profile_picture),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .width(56.dp)
                .height(56.dp)
        )
        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(
                text = userName,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = email,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            )
        }
    }
}


@Composable
fun SettingItemCard(
    modifier: Modifier = Modifier,
    mainText: String,
    subText: String,
    onClick: () -> Unit
) {
    Divider()
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .clickable { onClick() },
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mainText,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subText,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, settingsNav: NavHostController) {


    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item {
            UserBar(
                modifier = Modifier.padding(bottom = 16.dp),
                imageUrl = "",
                userName = "Test User",
                email = "email@Domain.com"
            )
        }
        item {
            val addresses by settingsViewModel.addresses.collectAsState()
            SettingItemCard(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Addresses",
                subText = "you have ${addresses.size} registered address(s)."
            ) {

            }
        }

        item {
            val wishlist by settingsViewModel.wishlist.collectAsState()
            SettingItemCard(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Wish List",
                subText = "you have ${wishlist.size} item(s) in wishlist"
            ) {

            }
        }
        item {
            val orders by settingsViewModel.orders.collectAsState()
            SettingItemCard(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Track Orders",
                subText = "you have ${orders.size} Order(s)."
            ) {

            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressDialog(
    dialogTitle: String,
    address: Address,
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
                style = TextStyle(textAlign = TextAlign.Center)
            )
        },
        text = {
            Column(Modifier.padding(4.dp)) {

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    placeholder = { Text(text = "ex +20 01122458") },
                    label = { Text(text = "Phone Number.") })

                OutlinedTextField(
                    value = buildingNumber,
                    onValueChange = { buildingNumber = it },
                    label = { Text(text = "buildingNumber") })

                OutlinedTextField(
                    value = streetName,
                    onValueChange = { streetName = it },
                    label = { Text(text = "Street Name") })

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text(text = "City") })

                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { zipCode = it },
                    label = { Text(text = "Zip Code") }
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
    /*     var showEditDialog by remember { mutableStateOf(false) }
        showEditDialog = true
        if (showEditDialog) {
            EditAddressDialog(
                dialogTitle = "Edit Address",
                address = Address(0,"","","","",""),
                dismissButtonText = "Cancel",
                confirmButtonText = "Save",
                onConfirm = {
                    settingsViewModel.updateAddress(it)
                },
                onDismiss = {
                    showEditDialog = false
                }
            )
        }*/

}


@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {
    SettingsScreen(SettingsViewModel(), rememberNavController())
}











