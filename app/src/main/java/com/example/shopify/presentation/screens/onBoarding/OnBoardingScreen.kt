package com.example.shopify.presentation.screens.onBoarding

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.SharedPreference.hasCompletedOnBoarding
import com.example.shopify.utilities.ShopifyApplication
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnBoardingScreen(navController: NavHostController) {
    val items = ArrayList<OnBoardingData>()

    items.add(
        OnBoardingData(
            R.drawable.online_shop,
            "Shop Awesome Products",
            "We have products in different categories For Men, Women and Kids"
        )
    )

    items.add(
        OnBoardingData(
            R.drawable.payment,
            "One Day Delivery",
            "Our delivery team around the clock to provide you the products fast and securely."
        )
    )

    items.add(
        OnBoardingData(
            R.drawable.favourites,
            "Add to Wishlist",
            "add all your favourite products to your wishlist to look back for it any time you want"
        )
    )


    val pagerState = rememberPagerState(
        pageCount = items.size,
        initialOffscreenLimit = 2,
        infiniteLoop = false,
        initialPage = 0,
    )
    val sharedPreference =
        (LocalContext.current.applicationContext as ShopifyApplication).sharedPreference
    if (sharedPreference.hasCompletedOnBoarding) {
        Log.i("menna", "true")
        LaunchedEffect(Unit) {
            navController.navigate(Screens.Home.route)
        }
    } else {
        Log.i("menna", "false")
        OnBoardingPager(items, pagerState, onComplete = {
            sharedPreference.hasCompletedOnBoarding = true
            navController.navigate(Screens.Home.route)
        })
    }
}

@ExperimentalPagerApi
@Composable
fun OnBoardingPager(
    item: List<OnBoardingData>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit
) {
    Box(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(state = pagerState) { page ->
                Column(
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = item[page].image),
                        contentDescription = item[page].title,
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                    )

                    Text(
                        text = item[page].title,
                        modifier = Modifier.padding(top = 50.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge
                    )

                    Text(
                        text = item[page].desc,
                        modifier = Modifier.padding(top = 30.dp, start = 20.dp, end = 20.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                }
            }
            Spacer(modifier.padding(40.dp))
            //Row() {
            PagerIndicator(item.size, pagerState.currentPage)

            Spacer(modifier.padding(10.dp))
            //                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomSection(pagerState.currentPage, onComplete)
            //              }
        }
    }
}

@Composable
fun PagerIndicator(
    size: Int,
    currentPage: Int,
    modifier: Modifier = Modifier.padding(top = 60.dp)
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(size) {
            Indicator(isSelected = it == currentPage)
        }

    }
}

@Composable
fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(targetValue = if (isSelected) 25.dp else 10.dp)

    Box(
        modifier = Modifier
            .padding(1.dp)
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
            )
    )
}

@Composable
fun BottomSection(currentPager: Int, onComplete: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = if (currentPager != 2) Arrangement.SpaceBetween else Arrangement.Center
    ) {

        if (currentPager == 2) {
            OutlinedButton(
                onClick = onComplete,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Get Started",
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 40.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            Text("Skip",
                Modifier
                    .padding(start = 20.dp)
                    .drawBehind {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height - 2.sp.toPx()
                        drawLine(
                            color = Color.Black,
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    })


            Text("Next", Modifier
                .padding(end = 20.dp)
                .drawBehind {
                    val strokeWidthPx = 1.dp.toPx()
                    val verticalOffset = size.height - 2.sp.toPx()
                    drawLine(
                        color = Color.Black,
                        strokeWidth = strokeWidthPx,
                        start = Offset(0f, verticalOffset),
                        end = Offset(size.width, verticalOffset)
                    )
                })


        }
    }
}

//
//@Preview
//@Composable
//fun OnBoarding(navController: NavHostController) {
//    //OnBoardingScreen()
//}