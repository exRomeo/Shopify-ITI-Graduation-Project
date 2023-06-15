package com.example.shopify.presentation.screens.homescreen


import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.core.navigation.Bottombar
import com.example.shopify.core.navigation.HomeNavGraph
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.navigation.TopBar
import com.example.shopify.core.navigation.bottomNavItems
import com.example.shopify.core.navigation.getNavController
import com.example.shopify.core.navigation.settingsnavigation.SettingsNavigation
import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.models.Varient
import com.example.shopify.data.repositories.product.IProductRepository

@Composable
fun HomeScreen(navController: NavHostController,padding:PaddingValues,modifier: Modifier = Modifier) {
//    ScaffoldStructure(screenTitle = "Home")
//    {
//HomeScreen(navController = navController )
//    }
  val repository: IProductRepository = (LocalContext.current.applicationContext as ShopifyApplication) .repository
     val viewModel: HomeViewModel = viewModel(factory =  HomeViewModelFactory(repository))

    val brandsState: UiState by viewModel.brandList.collectAsState()
    val randomsState: UiState by viewModel.randomList.collectAsState()
    var brandList: List<Brand> = listOf()
    var randomList: List<Varient> = listOf()
    when (brandsState) {
        is UiState.Loading -> {
            Log.i("menna","loading")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {
                CircularProgressIndicator(
                    modifier = Modifier.size(size = 64.dp),
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 6.dp
                )
            }

        }

        is UiState.Success<*> -> {
            Log.i("menna","success")
            brandList =
                (brandsState as UiState.Success<SmartCollections>).data.body()?.smart_collections!!
        }

        else -> {
            Log.i("homepage", (randomsState as UiState.Error).error.toString())
        }
    }
    when (randomsState) {
        is UiState.Loading -> {
        }

        is UiState.Success<*> -> {
            randomList = (randomsState as UiState.Success<Products>).data.body()?.products!!


        }

        else -> {

            Log.i("homepage", (randomsState as UiState.Error).error.toString())
        }
    }

    if (brandList.isNotEmpty() && randomList.isNotEmpty()) {
      //  ScaffoldStructure("home",navController) {
       // Scaffold (bottomBar = {Bottombar(navController = rememberNavController())}) {

            Column(
                modifier = modifier.padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues = PaddingValues(10.dp))
            )
            {


                HomeSection(sectionTitle = R.string.special_offers) {
                    AdsCarousel()
                }

                HomeSection(sectionTitle = R.string.brands) {

                    //  (brandsState as UiState.Success).data.body()?.let {
                    BrandCards(brands = brandList, navController = navController)
                }

                HomeSection(sectionTitle = R.string.trending_products) {
                    ItemCards(products = randomList,
                        isFavourite = true, onFavouriteClicked = {}, onAddToCard = {})

                }
            }
        }
        }


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScaffold(navController: NavHostController = rememberNavController()){
    val backStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(


        bottomBar = {
            if (backStackEntry?.destination?.route == "home"||(backStackEntry?.destination?.route == "categories")||
                backStackEntry?.destination?.route == "settings") {
                Bottombar(navController = navController)
            }
        }
    )

            {
        HomeNavGraph(navController = navController)
    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldStructure(
    screenTitle: String,
    navController: NavHostController,
    screen: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        topBar = {
            TopBar(title = screenTitle, onSearch = {})

        },
        content = {
            screen(it)
        },
        bottomBar = { Bottombar(navController = navController) }

    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDesign(
    modifier: Modifier = Modifier,
    onCardClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(modifier = modifier.padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        onClick = onCardClicked,
        content = {
            content()
        }
    )
}

@Composable
fun ItemCardContent(
    modifier: Modifier = Modifier, isFavourite: Boolean,
    onFavouritesClicked: (Boolean) -> Unit, onAddToCard: (item: Product) -> Unit, item: Varient
) {
    Column(modifier = modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
        item.image?.src?.let {
            ImageFromNetwork(image = it,
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Color.White)
                    .align(Alignment.CenterHorizontally)
                    .clickable {

                    }
            )
        }


        // Divider(thickness = 5.dp, color= Color.Black)
        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
            // var titles= item.title?.split("|")
            item.variants?.get(0)?.title.let {
                if (it != null) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            FavoriteButton(isFavourite = isFavourite, onClicked = onFavouritesClicked)

        }

        item.variants?.get(0)?.price.let {
            Text(
                text = it.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = { onAddToCard },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,

                )
        ) {
            Text(
                text = stringResource(R.string.add_to_cart),
                style = MaterialTheme.typography.bodyLarge
            )
        }

    }


}

@Composable
fun BrandCardContent(modifier: Modifier = Modifier, brand: Brand,navController:NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(15.dp)
            .clickable {
                Log.i("menna", brand.id.toString())

                navController.navigate("${Screens.Brands.route}/${brand.id}")


            }
    ) {

        brand.image?.src?.let {
            ImageFromNetwork(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(15.dp))
                    .background(color = Color.White),
                image = it
            )
        }

        brand.name?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }

}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AdsCarousel() {
    val items: List<String> = listOf(
        "https://www.seiu1000.org/sites/main/files/main-images/camera_lense_0.jpeg",
        "https://assets.keap.com/image/upload/v1547580346/Blog%20thumbnail%20images/Screen_Shot_2019-01-15_at_12.25.23_PM.png",
        "https://fitsmallbusiness.com/wp-content/uploads/2018/05/23-Best-Sales-Promotion-Ideas.png",
        "https://a.storyblok.com/f/140059/720x400/ab918c4aa1/sales-promotion-examples-to-help-boost-your-business-main.jpg"
    )

    val transform = ContentTransform(
        targetContentEnter = fadeIn(tween(durationMillis = 1000)),
        initialContentExit = fadeOut(tween(durationMillis = 1000))
    )

    Carousel(
        contentTransformForward = transform,
        contentTransformBackward = transform,
        slideCount = items.count(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(200.dp)
        // .clickable { onAddClick },
        ,
        content = { index ->
            items[index].also { item ->
                CarouselItem(
                    background = {

                        AsyncImage(
                            model = items[index],
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(15.dp))
                        )

                    },
                    content = {
                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            verticalArrangement = Arrangement.Bottom
                        ) {

                            Button(
                                modifier = Modifier
                                    .padding(10.dp),
                                shape = RoundedCornerShape(20.dp),
                                onClick = {
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary

                                )
                            ) {
                                Text(text = "Learm More")
                                //  }

                            }
                        }
                    }
                )
            }


        })
}


@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onClicked: (Boolean) -> Unit
) {
    Surface(
        shape = CircleShape,
        modifier = modifier
            .padding(6.dp)
            .size(32.dp),
        //color = Color.White
    ) {
        IconToggleButton(
            checked = isFavourite,
            onCheckedChange = onClicked

        ) {
            Icon(
                tint = MaterialTheme.colorScheme.primary,
                imageVector = if (isFavourite) {
                    Icons.Filled.Favorite
                } else {
                    Icons.Default.FavoriteBorder
                },
                contentDescription = "add to favourite"
            )
        }
    }
}

//"https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Adidas_Logo.svg/2560px-Adidas_Logo.svg.png"

@Composable
fun ImageFromNetwork(modifier: Modifier = Modifier, image: String) {
    val painter = rememberAsyncImagePainter(
        model = image,
        placeholder = painterResource(id = R.drawable.ic_launcher_background),
        error = painterResource(id = R.drawable.ic_launcher_background)
    )
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
    )
    Image(
        painter = painter,
        contentDescription = "custom transition based on painter state",
        contentScale = ContentScale.Inside,
        modifier = modifier
            .width(150.dp)
            .height(150.dp)
            .alpha(transition)
            .scale(.8f + (.2f * transition))
            .graphicsLayer { rotationX = (1f - transition) * 5f }
    )
}

@Composable
fun HomeSection(
    modifier: Modifier = Modifier,
    @StringRes sectionTitle: Int,
    sectionContent: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.padding(vertical = 20.dp)
    )
    {
        Text(
            text = stringResource(sectionTitle),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        )
        sectionContent()
    }
}

@Composable
fun BrandCards(modifier: Modifier = Modifier, brands: List<Brand>,navController:NavHostController) {
    LazyRow(
        modifier = modifier.padding(start = 6.dp, end = 6.dp)
    ) {
        items(brands) { item ->
            CardDesign(onCardClicked = {}) {
                BrandCardContent(brand = item, navController = navController)

            }

        }
    }
}

@Composable
fun ItemCards(
    modifier: Modifier = Modifier,
    products: List<Varient>,
    isFavourite: Boolean,
    onFavouriteClicked: (Boolean) -> Unit,
    onAddToCard: (item: Product) -> Unit
) {
    LazyRow(
        modifier = modifier.padding(start = 6.dp, end = 6.dp)
    ) {
        items(products) { item ->
            CardDesign(onCardClicked = {}) {
                ItemCardContent(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    isFavourite = isFavourite,
                    onFavouritesClicked = onFavouriteClicked,
                    onAddToCard = onAddToCard,
                    item = item
                )
            }

        }
    }
}







