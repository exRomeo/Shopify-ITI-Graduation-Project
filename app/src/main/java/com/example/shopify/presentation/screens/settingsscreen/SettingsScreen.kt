package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R


@Composable
fun UserBar(modifier: Modifier = Modifier, imageUrl: String, userName: String, email: String) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp), verticalAlignment = Alignment.CenterVertically
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
                .width(48.dp)
                .height(48.dp)
        )
        Spacer(modifier = Modifier.width(32.dp))

        Column {
            Text(
                text = userName,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
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
fun UserProfile() {
    var size by remember { mutableStateOf(350f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(orientation = Orientation.Vertical,
                state = rememberScrollableState { delta ->
                    if (size + delta < 350 && size + delta > 85)
                        size += delta
                    delta
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(size.dp)
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://www.gstatic.com/webp/gallery/1.jpg")
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.profile_picture_placehodler),
                alignment = Alignment.TopCenter,
                contentDescription = stringResource(id = R.string.profile_picture),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .clip(CircleShape)
            )
        }

        Text(
            "This is a header",
            Modifier
                .padding(20.dp, 10.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall
        )

        LazyColumn {
            item {
                Text(
                    "Start here...." + "lorem ipsum ".repeat(1200),
                    Modifier
                        .padding(20.dp, 10.dp, 20.dp, 5.dp)
                        .align(Alignment.CenterHorizontally),
                    lineHeight = 20.sp,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
fun CardComponent(
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
            .background(color = MaterialTheme.colorScheme.primaryContainer),
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
fun SettingsScreen() {
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
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Addresses",
                subText = "Ammar ebn yaser st"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }
        item {
            CardComponent(
                modifier = Modifier.padding(vertical = 8.dp),
                mainText = "Phone Numbers ",
                subText = "+201556003397"
            ) {}
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun SettingsPreview() {
    SettingsScreen()

}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
    UserProfile()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopupDialogExample(
    dialogTitle: String,
    dialogDesc: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onConfirm: (text: String) -> Unit
) {
    var fieldText by remember { mutableStateOf("") }
    // Create a mutable state for tracking whether the dialog is open or closed
    val showDialog = remember { mutableStateOf(true) }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = dialogTitle) },
            text = {
                Column(Modifier.padding(8.dp)) {
                    Text(text = "Phone Number.")
                    TextField(value = fieldText,
                        onValueChange = { fieldText = it },
                        placeholder = {
                            Text(
                                text = "ex +20 01122458"
                            )
                        })
                    Text(text = dialogDesc)
                    TextField(value = fieldText, onValueChange = { fieldText = it })
                    Text(text = dialogDesc)
                    TextField(value = fieldText, onValueChange = { fieldText = it })

                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm(fieldText)
                        showDialog.value = false
                    }
                ) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text(text = dismissButtonText)
                }
            },

            )
    } else {
        // Show a button to trigger the dialog
        Button(
            onClick = { showDialog.value = true }
        ) {
            Text(text = "Open Dialog")
        }
    }
}

@Composable
fun App() {
    Surface(color = MaterialTheme.colorScheme.background) {
        PopupDialogExample(
            dialogTitle = "Address",
            dialogDesc = "Edit Address.",
            confirmButtonText = "Save",
            dismissButtonText = "cancel"
        ) { Log.i("Alert Dialog", "App:$it ") }
    }
}

@Preview(device = "id:pixel_4")
@Composable
fun AppPreview() {
    App()
}






