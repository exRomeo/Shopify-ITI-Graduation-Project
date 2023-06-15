package com.example.shopify.presentation.screens.categories

import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.core.navigation.Bottombar
import com.example.shopify.core.navigation.TopBar
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.Varient
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.screens.brands.ProductsCards
import com.example.shopify.ui.theme.ShopifyTheme


val mainCategories = listOf("Men", "Women", "Kid", "Sale")
val list:List<Varient> = listOf(Varient(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png")),(Varient(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))),(Varient(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))),(Varient(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))),(Varient(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))))

val items = listOf(

    MiniFABItem(
        icon = R.drawable.hat,
        label = "ACCESSORIES",
    ),

    MiniFABItem(
        icon = R.drawable.shirt,
        label = "T-SHIRTS",
    ),

    MiniFABItem(
        icon = R.drawable.sneakers,
        label = "SHOES",
    )

)

@Composable
fun CategoriesScreen() {
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val viewModel: CategoriesViewModel = viewModel(
        factory = CategoriesViewModelFactory(
            repository
        )
    )

    val productsState: UiState by viewModel.productsList.collectAsState()
    var productsList: List<Varient> = listOf()
    when (productsState) {
        is UiState.Loading -> {

            LottieAnimation(animation = R.raw.loading_animation)

        }

        is UiState.Success<*> -> {
            productsList =
                (productsState as UiState.Success<Products>).data.body()?.products!!
            Log.i("menna", productsList.toString())
        }

        else -> {
            Log.i("homepage", (productsState as UiState.Error).error.toString())
        }
    }
    Scaffold(
        topBar = { TopBar(title = "bb", onSearch = {}) },
        bottomBar = { Bottombar(navController = rememberNavController())},
        //floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingButton(items = items, onMiniFABClicked = {

                viewModel.type = it
                viewModel.getProductsBySubcategory()

            })
        }


    ) {

        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainFilters(items = mainCategories, onItemSelection = { index ->

                when (index) {
                    0 -> {
                        Log.i("menna", "index 0")
                        viewModel.id = 449428980018
                        viewModel.getProductsBySubcategory()
                    }

                    1 -> {
                        viewModel.id = 449429012786

                    }

                    2 -> {
                        viewModel.id = 449429045554
                    }

                    else -> {
                        viewModel.id = 449429078322
                    }
                }


                viewModel.getProductsBySubcategory()
            }
            )
            if (productsList.isNotEmpty()) {
                viewModel.type = ""
                ProductsCards(
                    modifier = Modifier.height(600.dp),
                    products = productsList,
                    isFavourite = true,
                    onFavouriteClicked = {},
                    onAddToCard = {})
            }
            else{
                Text(text = "No results")
            }

        }
    }
}
    @Composable
    fun MainFilters(
        items: List<String>,
        defaultSelectedItemIndex: Int = 0,
        cornerRadius: Int = 30,
        onItemSelection: (selectedItemIndex: Int) -> Unit
    ) {

        val selectedIndex = remember { mutableStateOf(defaultSelectedItemIndex) }
        Row(
            //  modifier = Modifier.padding(start = 20.dp),
            // horizontalArrangement = Arrangement.Center
        ) {
            items.forEachIndexed { index, item ->
                OutlinedButton(
                    modifier = Modifier.width(100.dp),
                    onClick = {
                        selectedIndex.value = index
                        onItemSelection(selectedIndex.value)
                    },
                    shape = when (index) {
                        0 -> RoundedCornerShape(
                            topStartPercent = cornerRadius,
                            topEndPercent = 0,
                            bottomStartPercent = cornerRadius,
                            bottomEndPercent = 0
                        )

                        items.size - 1 -> RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = cornerRadius,
                            bottomStartPercent = 0,
                            bottomEndPercent = cornerRadius
                        )

                        else -> RoundedCornerShape(
                            topStartPercent = 0,
                            topEndPercent = 0,
                            bottomStartPercent = 0,
                            bottomEndPercent = 0
                        )
                    },
                    colors = if (selectedIndex.value == index) {
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )

                    } else {
                        ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
                    }
                ) {
                    Text(
                        text = item,
                        fontWeight = FontWeight.Bold,
                        color = if (selectedIndex.value == index) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )

                }


            }
        }
    }


    @Composable
    fun SliderComponent(onPriceValueChanged: (Float) -> Unit) {
        var sliderValue by remember {
            mutableStateOf(0f)
        }
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue_ ->
                sliderValue = sliderValue_
            },
            onValueChangeFinished = { onPriceValueChanged(sliderValue) },
            valueRange = 0f..1000f,
            steps = 9
        )

        Text(text = sliderValue.toString())
    }

    @Composable
    fun CategoriesItems() {
        val list = (1..10).map { it.toString() }

        LazyVerticalGrid(
            columns = GridCells.Adaptive(128.dp),
// content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(list.size) { index ->
                    Card(
                        // backgroundColor = Color.Red,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth(),
                        // elevation = CardElevation(),
                    ) {
                        Text(
                            text = list[index],
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        )
    }


    enum class FloatingButtonState {
        Expanded,
        Collapsed
    }

    data class MiniFABItem(
        val icon: Int,
        val label: String,
    )

    @Composable
    fun MiniFAB(
        modifier: Modifier = Modifier,
        item: MiniFABItem,
        onMiniFABClicked: (String) -> Unit
    ) {
        FloatingActionButton(
            modifier = modifier.size(40.dp),
            onClick = { onMiniFABClicked(item.label) },
            //  border = BorderStroke(1.dp, Color.Red),
            shape = CircleShape,
            //RoundedCornerShape(80), // = 50% percent
            // or shape = CircleShape
            containerColor = Color.Transparent
        ) {
            Image(
                painter = painterResource(item.icon),
                contentScale = ContentScale.Inside,
                //  Icon(modifier = Modifier.size(40.dp) ,
                //  painter = painterResource(R.drawable.accessories),
                contentDescription = "content description"
            )

        }

    }




    @Composable
    fun FloatingButton(
        items:List<MiniFABItem>,
          onMiniFABClicked: (String) -> Unit
    ) {
        var floatingButtonState by remember {
            mutableStateOf(FloatingButtonState.Collapsed)
        }


        val transition = updateTransition(targetState = floatingButtonState, label = "transition")
        val rotate by transition.animateFloat(label = "rotate") {
            if (it == FloatingButtonState.Collapsed) 0f else 315f
        }

        Column(
            //modifier = Modifier.alpha(if (transition.currentState == FloatingButtonState.Expanded) 1f else 0f)
            ) {
          //  if (transition.currentState == FloatingButtonState.Expanded) {
                items.forEach {

                    MiniFAB(modifier = Modifier.alpha(if (transition.currentState == FloatingButtonState.Expanded) 1f else 0f),
                        item = it, onMiniFABClicked = onMiniFABClicked)

                    Spacer(modifier = Modifier.size(16.dp))
                }





            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    Log.i("menna", "clicked")
                    //  onFloatingButtonStateChange(
                    if (transition.currentState == FloatingButtonState.Collapsed) {
                        floatingButtonState = FloatingButtonState.Expanded
                    } else {
                        floatingButtonState = FloatingButtonState.Collapsed

                    }
                },
                modifier = Modifier
                    .rotate(rotate)
                // .padding(20.dp)
            )
            {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(R.drawable.app),
                    contentDescription = "content description"
                )


            }        }

    }






@Preview
@Composable
fun MainFiltersPreview() {
    ShopifyTheme {

        CategoriesScreen()
       // Floatingbutton()

            }
        }



//SliderComponent({})
    //  MainFilters(items = mainCategories, onItemSelection = {})

