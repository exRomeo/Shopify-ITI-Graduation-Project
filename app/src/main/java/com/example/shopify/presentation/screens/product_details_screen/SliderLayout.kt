package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.repositories.product.IProductRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.yield
import kotlin.math.absoluteValue

@ExperimentalPagerApi
@Composable
fun SliderLayout() {
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val productDetailsViewModel: ProductDetailsViewModel =
        viewModel(factory = ProductDetailsViewModelFactory(repository))

    var product: SingleProduct? = null
    val productState: UiState by productDetailsViewModel.productInfoState.collectAsState()
    productDetailsViewModel.getProductInfo(8398828339506)
    var imageList: List<Image> = listOf()
    var image = listOf<Image>(
        Image(
            position = 1,
            src = "https://cdn.shopify.com/s/files/1/0771/5900/6514/products/85cc58608bf138a50036bcfe86a3a362.jpg?v=1685529028"
        ),
        Image(
            position = 2,
            src = "https://cdn.shopify.com/s/files/1/0771/5900/6514/products/8a029d2035bfb80e473361dfc08449be.jpg?v=1685529028"
        ),
        Image(
            position = 3,
            src = "https://cdn.shopify.com/s/files/1/0771/5900/6514/products/ad50775123e20f3d1af2bd07046b777d.jpg?v=1685529028"
        )
    )
    when (val state = productState) {
        is UiState.Success<*> -> {
            LaunchedEffect(key1 = state) {
                product =
                    (productState as UiState.Success<SingleProductResponseBody>).data.body()?.product
//                imageList = product?.images ?: listOf<Image>()
//
                if (product != null)
                    Log.i("TAG", "ProductDetailsScreen: $product")
                else
                    Log.i("TAG", "ProductDetailsScreen: NOT FOUND")
            }
        }

        is UiState.Error -> {
            LaunchedEffect(key1 = state) {
                val error = (productState as UiState.Error).error.message
                Log.i("TAG", "ProductDetailsScreen: $error")
            }
        }

        else -> {}
    }
    Surface(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

    }
    val pageState = rememberPagerState(pageCount = 3, initialPage = 1)
    LaunchedEffect(Unit) {
        while (true) {
            yield()
            delay(2000)
            pageState.animateScrollToPage(
                page = (pageState.currentPage + 1) % (pageState.pageCount),
                animationSpec = tween(600)
            )
        }
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "View Paged Slider Image",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        HorizontalPager(
            state = pageState,
            modifier = Modifier
                .weight(1f)
                .padding(0.dp, 40.dp, 0.dp, 40.dp)
        ) { page ->
            Card(modifier = Modifier
                .graphicsLayer {
                    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                    lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
                .fillMaxWidth()
                .padding(),
                shape = RoundedCornerShape(20.dp)) {
                if (image.isNotEmpty()) {
                    val new = image[page]
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.LightGray)
                            .align(alignment = Alignment.CenterHorizontally)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(new.src)
                                .placeholder(R.drawable.product_image_placeholder)
                                .error(R.drawable.product_image_placeholder)
                                .crossfade(true)
                                .build(),
                            contentDescription = stringResource(id = R.string.product_image),
                            placeholder = painterResource(id = R.drawable.product_image_placeholder),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .size(125.dp)
                        )
                    }
                    HorizontalPagerIndicator(
                        pagerState = pageState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}