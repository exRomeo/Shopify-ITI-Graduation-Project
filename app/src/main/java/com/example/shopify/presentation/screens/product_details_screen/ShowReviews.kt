package com.example.shopify.presentation.screens.product_details_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import com.example.shopify.data.models.Reviews
import com.example.shopify.ui.theme.hintColor
import kotlin.random.Random

@Composable
fun ShowReviews(
    isShowReview: Boolean,
    onDismiss: () -> Unit
) {
    val reviewList = getReviews()
    if (isShowReview) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = stringResource(id = R.string.review))
            },
            text = {
                LazyColumn() {
                    items(reviewList.size) {
                        OneItemReview(
                            nameOfCustomer = reviewList[it].nameOfCustomer,
                            rating = reviewList[it].rating,
                            review = reviewList[it].body,
                            date = reviewList[it].date
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(id = R.string.close))
                }
            }
        )
    }

}

@Composable
fun OneItemReview(
    nameOfCustomer: String,
    rating: Double,
    review: String,
    date: String,

    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(MaterialTheme.shapes.small)
//                            .padding(16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.user),
                            contentDescription = stringResource(id = R.string.profile_picture),
                        )
                    }
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = nameOfCustomer)
                    Spacer(modifier = Modifier.width(2.dp))
                   Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                       Text(text = rating.toString())
                       Spacer(modifier = Modifier.width(1.dp))
                       Icon(
                           modifier = Modifier.size(16.dp),
                           painter = painterResource(id = R.drawable.filled_star),
                           contentDescription = stringResource(id = R.string.half_star),
                           tint = Color.Yellow
                       )
                   }
                }
                Text(
                    text = date,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleSmall.fontSize,
                        color = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = review,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                )

            }
        }
    }
}

fun getReviews(): List<Reviews> {
    return listOf(
        Reviews(
            nameOfCustomer = "Nada Elshafey",
            rating = 4.8,
            body = "Just writing a rare review on these . I've ordered and returned a lot. Tried again lol and these ARE AMAZING comfortable. So much that I may order 3 more this year just to have them. The color is so cute and clean . I'm 99.9% sure I've finally found a product I'm not going to return",
            date = "Reviewed in Egypt on  Jan 08, 2023",
        ),
        Reviews(
            nameOfCustomer = "Menna Ihab",
            rating = 5.0,
            body = "I recently purchased the product and I am extremely happy with my purchase. The product's design is sleek and modern..",
            date = "Reviewed in Emirates on March 29, 2023",
        ),
        Reviews(
            nameOfCustomer = "Ramy Ashraf",
            rating = 4.5,
            body = "I highly recommend the product for anyone in the market for high-quality. However, I would caution against relying on the seller's shipping services, as my experience was frustrating and disappointing.",
            date = "Reviewed in Egypt on  July 31, 2023",
        ),
        Reviews(
            nameOfCustomer = "Amber.s",
            rating = 5.0,
            body = "I’m very satisfied. They are high-quality and worth the money. The store also offered free shipping at that price so that’s a plus!",
            date = "Reviewed in the United States US on May 17, 2022",
        ),
        Reviews(
            nameOfCustomer = "Sarah Aga",
            rating = 3.5,
            body = "In the pictures and description, there was no clear. . Definitely disappointed I had to wait to receive this before knowing what it contained. Attached are some pictures so you know what type of blocks are inside.",
            date = "Reviewed in Egypt on April 18, 2021",
        ),
        Reviews(
            nameOfCustomer = "Caroline",
            rating = 5.0,
            body = " Highly recommended!.The app was user-friendly, making it easy to find the perfect item. The checkout process was smooth, and I received my order promptly. The product arrived in excellent condition, exactly as described on their website. I’m thrilled with the quality and will definitely shop at store again in the future..",
            date = "Reviewed in the France on February 24, 2020",
        ),

        Reviews(
            nameOfCustomer = "Diane Johnson",
            rating = 4.8,
            body = "I love the product they are true to size , and I got just that",
            date = "Reviewed in Brazil on Sep 18, 2018",
        ),
    )
}