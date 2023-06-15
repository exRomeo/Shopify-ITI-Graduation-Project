package com.example.shopify.presentation.screens.product_details_screen


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.presentation.common.composables.CustomCornerButton
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.ui.theme.IbarraFont
import com.example.shopify.ui.theme.hintColor
import com.example.shopify.ui.theme.ibarraBold
import com.example.shopify.ui.theme.ibarraRegular
import com.example.shopify.ui.theme.mainColor
import kotlin.random.Random

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailsContentScreen(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onFavoriteChanged: () -> Unit,
    onAcceptFavChanged: () -> Unit,
    itemCount: Int,
    increase: () -> Unit,
    decrease: () -> Unit,
    showDialog: Boolean,
    showFavWarningDialog: Boolean,
    showReviewsDialog : Boolean,
    onShowDialogAction: () -> Unit,
    onShowFavDialogAction: () -> Unit,
    showReviews: ()->Unit,
    product: SingleProduct
) {
    Log.i("TAG", "ProductDetailsContentScreen: $product")
    Scaffold { values ->
        LazyColumn(contentPadding = values) {
            items(1) {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    initialPageOffsetFraction = 0f
                ) {
                    product.images?.size ?: 0
                }
                val colorMatrix = remember { ColorMatrix() }

                Card(
                    modifier = modifier.padding(bottom = 50.dp),
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
                        state = pagerState
                    ) { index ->
                        val pageOffset =
                            (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
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
                        Box {
                            AsyncImage(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.7f)
                                    .aspectRatio(3f / 2f)
                                    .graphicsLayer {
                                        scaleX = imageSize
                                        scaleY = imageSize
                                    },
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(product.images?.get(index)?.src)
                                    .placeholder(R.drawable.product_image_placeholder)
                                    .error(R.drawable.product_image_placeholder)
                                    .build(),
                                contentDescription = "Image",
                                contentScale = ContentScale.FillBounds,
                                colorFilter = ColorFilter.colorMatrix(colorMatrix)
                            )
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .size(32.dp)
                            ) {
                                IconButton(onClick = {
                                    Log.i("TAG", "back to previous screen")
                                }) {
                                    Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = stringResource(
                                            id = R.string.back
                                        )
                                    )
                                }
                            }
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(32.dp)
                            ) {
                                IconButton(onClick = onFavoriteChanged) {
                                    if (isFavorite) Icon(
                                        Icons.Default.Favorite, contentDescription = stringResource(
                                            id = R.string.is_fav
                                        )
                                    ) else Icon(
                                        painter = painterResource(id = R.drawable.is_not_fav),
                                        contentDescription = stringResource(
                                            id = R.string.is_not_fav
                                        )
                                    )
                                }
                            }
                        }

                    }
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                modifier = Modifier.weight(1.5f),
                                text = product.title ?: "",
                                style = ibarraBold,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                minLines = 2,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .align(Alignment.CenterVertically),
                                text = "${product.variants?.get(0)?.price} $",
                                style = ibarraBold,
                                fontWeight = FontWeight.Bold,
                                color = mainColor,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier.fillMaxWidth()) {
                            RatingBar(
                                rating = Random.nextDouble(3.0, 5.0),
                                stars = 5,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { showReviews
                                Log.i("TAG", "Show Review: ")}) {
                                Text(
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically),
                                    text = stringResource(id = R.string.show_reviews),
                                    style = ibarraBold,
                                    fontWeight = FontWeight.Normal,
                                    color = hintColor,
                                    fontSize = 16.sp,
                                )
                            }

                        }


                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.size),
                            style = ibarraBold,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        SizeOptions(product.options ?: listOf())
                        Spacer(modifier = Modifier.height(8.dp))

                        Divider(
                            color = MaterialTheme.colorScheme.surfaceTint,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.color),
                            style = ibarraBold,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        ColorOptions(product.options ?: listOf())
                        Spacer(modifier = Modifier.height(16.dp))

                        Divider(
                            color = MaterialTheme.colorScheme.surfaceTint,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(id = R.string.description),
                            style = ibarraBold,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = product.description ?: "",
                            style = ibarraRegular,
                            color = Color.Black,
                            fontSize = 18.sp
                        )

                    }
                }
            }
        }
        if (showDialog) {
            WarningDialog(
                dialogTitle = stringResource(id = R.string.remove_product),
                message = stringResource(id = R.string.cart_item_removal_warning),
                dismissButtonText = stringResource(id = R.string.cancel),
                confirmButtonText = stringResource(id = R.string.remove),
                onConfirm = onShowDialogAction,
                onDismiss = decrease

            )
        }
        if (showFavWarningDialog) {
            val title: String?
            val message: String
            val confirmButtonText: String
            if (isFavorite) {
                title = stringResource(id = R.string.remove_product_from_fav)
                message = stringResource(id = R.string.fav_item_removal_warning)
                confirmButtonText = stringResource(id = R.string.remove)
            } else {
                title = stringResource(id = R.string.add_product_to_fav)
                message = stringResource(id = R.string.add_item_to_fav_message)
                confirmButtonText = stringResource(id = R.string.add)
            }
            WarningDialog(
                dialogTitle = title,
                message = message,
                dismissButtonText = stringResource(id = R.string.cancel),
                confirmButtonText = confirmButtonText,
                onConfirm = onAcceptFavChanged,
                onDismiss = onShowFavDialogAction
            )

        }
        if(showReviewsDialog)
            ShowReviews()
    }
    Box() {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                    .background(mainColor)
                    .width(200.dp)
                    .clickable { Log.i("TAG", "Add to cart: ") }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { Log.i("TAG", "Add to cart: ") }
                    ) {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            stringResource(id = R.string.add_to_cart),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.add_to_cart),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )

                    )
                }
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .width(125.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = increase
                    ) {
                        Icon(
                            Icons.Default.Add,
                            stringResource(id = R.string.add)
                        )
                    }
                    Text(
                        text = itemCount.toString(),
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            textAlign = TextAlign.Center
                        )

                    )
                    Log.i("TAG", "ProductDetailsContentScreen: $itemCount")
                    IconButton(
                        onClick = decrease

                    ) {
                        Icon(
                            painterResource(id = R.drawable.remove),
                            stringResource(id = R.string.remove)
                        )

                    }

                }
            }
        }
    }

}