package com.example.shopify.presentation.screens.categories

import android.util.Log
import androidx.annotation.DrawableRes
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.core.navigation.Bottombar

import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.Variant
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.common.composables.CustomSearchbar
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.screens.brands.ProductsCards
import com.example.shopify.ui.theme.ShopifyTheme


val mainCategories = listOf("Men", "Women", "Kid", "Sale")
val list:List<Variant> = listOf(Variant(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png")),(Variant(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))),(Variant(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))),(Variant(1,"ppp", listOf(Product(
    1,1,"adidas","200")),Image(src = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/640px-Image_created_with_a_mobile_phone.png"))),(Variant(1,"ppp", listOf(Product(
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
    ),
            MiniFABItem(
            icon = R.drawable.close,
           label = "CLOSE",
)

)

@Composable
fun CategoriesScreen(navController:NavHostController) {
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val viewModel: CategoriesViewModel = viewModel(
        factory = CategoriesViewModelFactory(
            repository
        )
    )

    val productsState: UiState by viewModel.productsList.collectAsState()
    var productsList: List<Variant> = listOf()
    var filteredList:List<Variant> = listOf()
     var state:String = "fail"
   // var FABIcon = R.drawable.ic_category
    var FABIcon by rememberSaveable {
        mutableStateOf( R.drawable.app)

    }
    var searchText by remember { mutableStateOf("") }
    val isSearching by remember {
        derivedStateOf {
            searchText.isNotEmpty()
        }
    }

    var p = 0f
    var productPrice by rememberSaveable {
        mutableStateOf(p)
    }
    var filteredState by rememberSaveable {
        mutableStateOf(filteredList)

    }
    val searchFilteredList by remember {
        derivedStateOf {
            if (searchText.isEmpty()) {
                filteredState
            } else {
                filteredState.filter { it.title?.startsWith(searchText, ignoreCase = true) ?: false }
            }
        }
    }
    var floatingButtonState by rememberSaveable {
        mutableStateOf(FloatingButtonState.Collapsed)
    }

    when (productsState) {
        is UiState.Loading -> {
            LottieAnimation(animation = R.raw.loading_animation)
        }

        is UiState.Success<*> -> {
            state = "success"
            productsList =
                (productsState as UiState.Success<Products>).data.body()?.products!!
            filteredState =  productsList.filter { item ->
                //  item.variants?.get(0)?.price?.let { it1 -> Log.i("hla", it1) }
                item.variants?.get(0)?.price?.toFloat()!! >= productPrice
            }
            Log.i("hala",productPrice.toString())

           // filteredState = productsList
         //   Log.i("menna", productsList.toString())
        }

        else -> {
            Log.i("homepage", (productsState as UiState.Error).error.toString())
        }
    }
    Scaffold(
        bottomBar = { Bottombar(navController = navController)},
        //floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingButton(items = items, onMiniFABClicked = {

                viewModel.type = it
                viewModel.getProductsBySubcategory()
                floatingButtonState = FloatingButtonState.Collapsed
                FABIcon = when (it){
                    "ACCESSORIES" -> {
                        R.drawable.hat
                    }

                    "T-SHIRTS" -> {
                        R.drawable.shirt
                    }

                    "SHOES" ->{
                        R.drawable.sneakers
                    }

                    else -> {
                        viewModel.type = ""
                        R.drawable.app
                    }
                }




            }, onFABCLicked =
            {

                    floatingButtonState = FloatingButtonState.Expanded


                },FABIcon,floatingButtonState
            )
        }


    ) {

        Column(
            modifier = Modifier.padding(it),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomSearchbar(
                searchText = searchText,
                onTextChange = { searchText = it },
                hintText = R.string.search_brands,
                isSearching = isSearching,
                onCloseSearch = { searchText = "" }
            )
            Spacer(modifier = Modifier.height(8.dp))
            MainFilters(items = mainCategories, onItemSelection = { index ->

                when (index) {
                    0 -> {
                        Log.i("menna", "index 0")
                        viewModel.id = 449428980018

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

            SliderComponent { price ->

                p = price
                    productPrice = p
                Log.i("hala",productPrice.toString())

                filteredState =  productsList.filter { item->
                  //  item.variants?.get(0)?.price?.let { it1 -> Log.i("hla", it1) }
                    item.variants?.get(0)?.price?.toFloat()!! >= price

                }

              //  viewModel.getProductsBySubcategory()
               // Log.i("hla", price.toString())
                Log.i("hla",filteredList.toString())
                filteredState
            }
            if (filteredState.isNotEmpty()) {
               // viewModel.type = ""
                ProductsCards(
                    navController = navController,
                    modifier = Modifier.height(600.dp),
                    products = searchFilteredList,
                    isFavourite = true,
                    onAddToCard = {})
            }
            else{

                if(state == "success" &&filteredState.isEmpty()){
                    Column {
                        Image(
                            modifier = Modifier.size(200.dp),
                            painter = painterResource(R.drawable.search),
                            contentDescription = "content description"
                        )

                        Text(text = "No results Found")

                    }

                }
                else {
                    LottieAnimation(animation = R.raw.loading_animation)
                }
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
    fun SliderComponent(onPriceValueChanged: (Float) -> List<Variant>) {
        var sliderValue by remember {
            mutableStateOf(0f)
        }
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue_ ->
                sliderValue = sliderValue_
            },
            onValueChangeFinished = { onPriceValueChanged(sliderValue) },
            valueRange = 0f..250f,
            steps = 4
        )
        Text(text = "> $sliderValue")
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
            onClick = { onMiniFABClicked(item.label)
                      Log.i("hla","mini clicked")},
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
          onMiniFABClicked: (String) -> Unit,
         onFABCLicked:()->Unit,
        @DrawableRes resId: Int,
        floatingButtonState:FloatingButtonState

    ) {



        val transition = updateTransition(targetState = floatingButtonState, label = "transition")
        val rotate by transition.animateFloat(label = "rotate") {
            if (it == FloatingButtonState.Collapsed) 0f else 315f
        }

        Column(
            //modifier = Modifier.alpha(if (transition.currentState == FloatingButtonState.Expanded) 1f else 0f)
            ) {
            if (transition.currentState == FloatingButtonState.Expanded) {
                items.forEach {

                    MiniFAB(
                        modifier = Modifier
                        //   .alpha(if (transition.currentState == FloatingButtonState.Expanded) 1f else 0f
                        ,
                        item = it, onMiniFABClicked = onMiniFABClicked
                    )

                    Spacer(modifier = Modifier.size(16.dp))
                }
            }





            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                onClick =
                //{
                   // Log.i("menna", "clicked")
                     onFABCLicked
                    //  onFloatingButtonStateChange(
//                    if (transition.currentState == FloatingButtonState.Collapsed) {
//                        floatingButtonState = FloatingButtonState.Expanded
//                    } else {
//                        floatingButtonState = FloatingButtonState.Collapsed
//
//                    }
              //  }
        ,
                modifier = Modifier
                    .rotate(rotate)
                // .padding(20.dp)
            )
            {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(resId),
                    contentDescription = "content description"
                )


            }        }

    }


@Preview
@Composable
fun MainFiltersPreview() {
    ShopifyTheme {

     //   CategoriesScreen()
       // Floatingbutton()

            }
        }



//SliderComponent({})
    //  MainFilters(items = mainCategories, onItemSelection = {})

