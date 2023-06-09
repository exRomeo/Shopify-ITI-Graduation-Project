package com.example.shopify.presentation.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SingleSelectDropdownMenu() {
    var selection by remember { mutableStateOf("choose a currency :D") }
    var expandedState by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        ExposedDropdownMenuBox(
            expanded = expandedState,
            onExpandedChange = { expandedState = !expandedState },
        ) {
            OutlinedTextField(
                value = selection,
                onValueChange = {},
                placeholder = { Text(text = "Currency")},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expandedState
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = expandedState,
                onDismissRequest = { expandedState = false }) {
                Currency.list.forEach {
                    DropdownMenuItem(
                        text = { Text(modifier = Modifier.fillMaxWidth(),text = "${it.country}\n${it.symbol}", style = TextStyle(textAlign = TextAlign.Center)) },
                        onClick = {
                            selection = it.country
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