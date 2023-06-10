package com.example.shopify.presentation.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.shopify.data.models.Currency
import com.example.shopify.data.models.ItemWithName
import com.example.shopify.presentation.screens.settingsscreen.TAG

@Preview(showSystemUi = true)
@Composable
fun MenuPrev() {
    SingleSelectionDropdownMenu(title = "Choose Currency", items = Currency.list) {
        Log.i(TAG, "MenuPrev: $it")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SingleSelectionDropdownMenu(
    title: String,
    items: List<T>,
    onSelect: (index: Int) -> Unit
) where T : ItemWithName {
    var selection by remember { mutableStateOf(title) }
    var expandedState by remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = { expandedState = !expandedState },
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                value = selection,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedState
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
            ExposedDropdownMenu(
                expanded = expandedState,
                onDismissRequest = { expandedState = false }) {
                items.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = it.getItemName(),
                                style = TextStyle(textAlign = TextAlign.Center)
                            )
                        },
                        onClick = {
                            selection = it.getShortName()
                            onSelect(items.indexOf(it))
                            expandedState = false
                        })
                }
            }

        }
    }
}


//
//@OptIn(ExperimentalMaterial3Api::class)
//@Preview
//@Composable
//fun SingleSelectDropdownMenu() {
//    var selection by remember { mutableStateOf("choose a currency :D") }
//    var expandedState by remember { mutableStateOf(false) }
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        TextField(
//            value = selection,
//            onValueChange = {},
//            readOnly = true,
//            trailingIcon = {
//                ExposedDropdownMenuDefaults.TrailingIcon(
//                    expanded = expandedState
//                )
//            },
//            colors = ExposedDropdownMenuDefaults.textFieldColors()
//        )
//        DropdownMenu(
//            expanded = expandedState,
//            onDismissRequest = { expandedState = false }
//        ) {
//            LazyColumn() {
//                items(Currency.list) {
//                    DropdownMenuItem(
//                        text = { Text(text = "${it.country}\n${it.symbol}") },
//                        onClick = {
//                            selection = it.symbol
//                            expandedState = false
//                        })
//                }
//            }
//        }
//    }
//}