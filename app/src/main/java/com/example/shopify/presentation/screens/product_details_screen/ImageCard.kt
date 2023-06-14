package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R
import com.example.shopify.data.models.Currency
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.presentation.common.composables.SingleSelectionDropdownMenu
import com.example.shopify.presentation.screens.settingsscreen.TAG
import com.example.shopify.ui.theme.ibarraBold
import com.example.shopify.ui.theme.mainColor

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun ImageCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    product: SingleProduct? = null,
    productImages : List<Image>
) {
    val pagerState = rememberPagerState()
    val colorMatrix = remember { ColorMatrix() }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
    )
    {
        HorizontalPager(
            pageCount = productImages.size,
            state = pagerState
        ) { index ->
            val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            val imageSize by animateFloatAsState(
                targetValue = if (pageOffset != 0.0f) 0.75f else 1f,
                animationSpec = tween(durationMillis = 300)
            )
            LaunchedEffect(key1 = imageSize) {
                if (pageOffset != 0.0f) {
                    colorMatrix.setToSaturation(0f)
                } else {
                    colorMatrix.setToSaturation(1f)
                }
            }
            AsyncImage(
                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
//                    .size(200.dp)
//                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
                    .aspectRatio(3f / 2f)
                    .graphicsLayer {
                        scaleX = imageSize
                        scaleY = imageSize
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productImages[index].src)
                    .placeholder(R.drawable.product_image_placeholder)
                    .error(R.drawable.product_image_placeholder)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.FillBounds,
//                contentScale = ContentScale.Fit, *****
//                contentScale = ContentScale.Crop, *****
//                contentScale = ContentScale.Inside,
//                contentScale = ContentScale.FillWidth, **********
//                contentScale = ContentScale.FillHeight,

                colorFilter = ColorFilter.colorMatrix(colorMatrix)
            )

        }

        /* AsyncImage(
             modifier = Modifier
 //                    .fillMaxSize()
                 .clip(MaterialTheme.shapes.large)
                 .fillMaxSize()
                 .aspectRatio(3f / 2f),
 //              .graphicsLayer {
 //                  scaleX = imageSize
 //                  scaleY = imageSize
 //              },
             model = ImageRequest.Builder(LocalContext.current)
                 .data(image.src)
                 .placeholder(R.drawable.product_image_placeholder)
                 .error(R.drawable.product_image_placeholder)
                 .build(),
             contentDescription = "Image",
             contentScale = ContentScale.Crop,
 //          colorFilter = ColorFilter.colorMatrix(colorMatrix)
         )*/
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row() {
                Text(
                    text = "Title $title",
                    style = ibarraBold,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    minLines = 2,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Price $50",
                    style = ibarraBold,
                    fontWeight = FontWeight.Bold,
                    color = mainColor,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Description",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxSize(),
            ) {
                AssistChip(
                    onClick = { /*TODO*/ },
                    colors = AssistChipDefaults.assistChipColors(
                        leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.FavoriteBorder,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Mark as favorite")
                    }
                )
                AssistChip(
                    onClick = { /*TODO*/ },
                    colors = AssistChipDefaults.assistChipColors(
                        leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Share with others")
                    }
                )
            }
        }
    }
}
