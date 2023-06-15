package com.example.shopify.presentation.screens.product_details_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.shopify.data.models.Options


@Composable
fun SizeOptions(option: List<Options>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var selectedIndex by remember { mutableIntStateOf(-1) }
        option.forEachIndexed { index, item ->
            if (option[index].name == "Size") {
                LazyRow {
                    items(1) {
                        option[index].values?.forEachIndexed { sizeIndex, _ ->
                            OutlinedButton(
                                onClick = { selectedIndex = sizeIndex },
                                modifier = when (sizeIndex) {
                                    0 ->
                                        Modifier
                                            .offset(0.dp, 0.dp)
                                            .zIndex(if (selectedIndex == sizeIndex) 1f else 0f)

                                    else ->
                                        Modifier
                                            .offset((-1 * sizeIndex).dp, 0.dp)
                                            .zIndex(if (selectedIndex == sizeIndex) 1f else 0f)
                                },
                                shape = RoundedCornerShape(
                                    topStart = 5.dp,
                                    topEnd = 5.dp,
                                    bottomStart = 5.dp,
                                    bottomEnd = 5.dp
                                ),
                                border = BorderStroke(
                                    1.dp, if (selectedIndex == sizeIndex) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
                                    }
                                ),
                                colors = if (selectedIndex == sizeIndex) {
                                    ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primary.copy(
                                            alpha = 0.1f
                                        ),
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    ButtonDefaults.outlinedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.primary
                                    )
                                }
                            ) {
                                Text(option[index].values?.get(sizeIndex) ?: "")
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                    }
                }

            }
        }
    }
}