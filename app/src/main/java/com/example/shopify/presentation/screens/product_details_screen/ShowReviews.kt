package com.example.shopify.presentation.screens.product_details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import kotlin.random.Random

@Composable
fun ShowReviews() {
    val openDialog = remember { mutableStateOf(false) }
    val itemsList = listOf<String>("First", "Second")
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = "Select an item")
            },
            text = {
                LazyColumn() {
                    items(itemsList.size) {
                        OneItemReview(
                            nameOfCustomer = itemsList[it],
                            rating = Random.nextDouble(
                                from = 2.0,
                                until = 5.0
                            )
                            , review = itemsList[it])
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }

}

@Composable
fun OneItemReview(
    nameOfCustomer: String,
    rating: Double,
    review: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box {
            Column() {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(MaterialTheme.shapes.small)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_boy),
                            contentDescription = "contentDescription",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = nameOfCustomer)
                    Spacer(modifier = Modifier.width(8.dp))
                    RatingBar(rating = rating)
                }

                Text(
                    text = review,
                    style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize)
                )

            }
        }
    }
}