package com.example.shopify.presentation.screens.homescreen


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.shopify.R
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.DiscountHelper
import com.example.shopify.core.helpers.UiState
import com.example.shopify.core.navigation.Bottombar
import com.example.shopify.core.navigation.Screens
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.common.composables.CustomSearchbar
import com.example.shopify.presentation.common.composables.ShowCustomDialog
import com.example.shopify.presentation.common.composables.WarningDialog
import com.example.shopify.utilities.ShopifyApplication

@Composable
fun HomeScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val authRepository: IAuthRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).authRepository
    val wishlistManager: WishlistManager =
        (LocalContext.current.applicationContext as ShopifyApplication).wishlistManager
    val cartManager: CartManager =
        (LocalContext.current.applicationContext as ShopifyApplication).cartManager
    val viewModel: HomeViewModel =
        viewModel(factory = HomeViewModelFactory(repository, wishlistManager, cartManager))
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        CurrentUserHelper.initialize(authRepository)
        DiscountHelper.initialize(context.applicationContext)
    }
    val brandsState: UiState by viewModel.brandList.collectAsState()
    val randomsState: UiState by viewModel.randomList.collectAsState()
    var brandList: List<Brand> by remember { mutableStateOf(listOf()) }
    var randomList: List<ProductSample> by remember { mutableStateOf(listOf()) }
    var searchText by remember { mutableStateOf("") }
    val isSearching by remember {
        derivedStateOf {
            searchText.isNotEmpty()
        }
    }
    val filteredList by remember {
        derivedStateOf {
            if (searchText.isEmpty()) {
                brandList
            } else {
                brandList.filter { it.name?.startsWith(searchText, ignoreCase = true) ?: false }
            }
        }
    }

    var openDialogue by remember {
        mutableStateOf(false)
    }
    when (brandsState) {
        is UiState.Loading -> {
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
            brandList = (brandsState as UiState.Success<SmartCollections>).data.smart_collections!!
        }

        else -> {
            Log.i("homepage", (randomsState as UiState.Error).error.toString())
        }
    }
    when (randomsState) {
        is UiState.Loading -> {
        }

        is UiState.Success<*> -> {
            randomList = (randomsState as UiState.Success<Products>).data.products!!
        }

        else -> {
            Log.i("homepage", (randomsState as UiState.Error).error.toString())
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect {
            snackbarHostState.showSnackbar(context.getString(it))
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                title = stringResource(id = R.string.app_name),
                onCartClick = {
                    if (CurrentUserHelper.isLoggedIn())
                        navController.navigate(Screens.Cart.route)
                    else
                        viewModel.showMessage(R.string.need_to_be_logged_in)
                },
                onWishlistClick = {
                    if (CurrentUserHelper.isLoggedIn())
                        navController.navigate(Screens.Wishlist.route)
                    else
                        viewModel.showMessage(R.string.need_to_be_logged_in)
                })
        },
        bottomBar = { Bottombar(navController = navController) }
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        if (brandList.isNotEmpty() && randomList.isNotEmpty()) {
            Column(
                modifier = modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues = PaddingValues(10.dp))
            )
            {
                CustomSearchbar(
                    searchText = searchText,
                    onTextChange = { searchText = it },
                    hintText = R.string.search_brands,
                    isSearching = isSearching,
                    onCloseSearch = { searchText = "" }
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (!isSearching)
                    HomeSection(sectionTitle = R.string.special_offers) {
                        AdsCarousel()
                    }


                HomeSection(sectionTitle = R.string.brands) {
                    BrandCards(brands = filteredList, navController = navController)
                }

                Spacer(modifier = Modifier.padding(top = 20.dp))
                HomeSection(sectionTitle = R.string.trending_products) {
                    Spacer(modifier = Modifier.padding(bottom = 5.dp))
                    ItemCards(navController = navController, viewModel, products = randomList,
                        isFavourite = false, onFavouriteClicked = {
                        }, onAddToCard = { product ->
                            if (CurrentUserHelper.isLoggedIn()) {
                                viewModel.addItemToCart(product.id, product.variants[0].id)
                            } else {
                                openDialogue = true

                            }

                        })
                    if (openDialogue) {
                        Surface(color = Color.Gray) {
                            ShowCustomDialog(
                                title = R.string.login,
                                description = R.string.please_login,
                                buttonText = R.string.login,
                                animatedId = R.raw.sign_for_error_or_explanation_alert,
                                buttonColor = MaterialTheme.colorScheme.error,
                                onClickButton = { navController.navigate(Screens.Login.route) },
                                onClose = { openDialogue = false }
                            )
                        }
                    }

                }
            }
        }
    }
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
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isFavourite: Boolean,
    onFavouritesClicked: (item: ProductSample) -> Unit,
    onAddToCard: (item: ProductSample) -> Unit,
    item: ProductSample
) {
    Column(modifier = modifier
        .clickable {
            navController.navigate(route = "${Screens.Details.route}/${item.id}")
        }
        .padding(horizontal = 10.dp, vertical = 10.dp)
        .height(330.dp)
        .width(200.dp)) {
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

        // var titles= item.title?.split("|")
        Text(
            modifier = Modifier
                .width(200.dp)
                .padding(top = 5.dp),
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Row(horizontalArrangement = Arrangement.spacedBy(60.dp)) {

            Text(
                text = item.variants[0].price.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FavoriteButton(
                isFavourite = isFavourite,
                onClicked = { onFavouritesClicked(item) })

        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp),
            shape = RoundedCornerShape(20.dp),
            onClick = { onAddToCard(item) },
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
fun BrandCardContent(
    modifier: Modifier = Modifier,
    brand: Brand,
    navController: NavHostController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(15.dp)
            .clickable {
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
fun AdsCarousel(
) {
    val items: List<String> = listOf(
        "https://img.freepik.com/free-psd/summer-sale-70-discount_23-2148476960.jpg",
        "https://assets.keap.com/image/upload/v1547580346/Blog%20thumbnail%20images/Screen_Shot_2019-01-15_at_12.25.23_PM.png",
        "https://fitsmallbusiness.com/wp-content/uploads/2018/05/23-Best-Sales-Promotion-Ideas.png",
        "https://a.storyblok.com/f/140059/720x400/ab918c4aa1/sales-promotion-examples-to-help-boost-your-business-main.jpg"
    )

    val couponsList: List<String> =
        listOf("Cf56u%erdHJJ", "AAgf8876GFd", "GRTO#kl76C", "PTYR%&R#dw")
    val offers: List<Int> = listOf(5, 20, 50, 70)
    var openDialogue by remember {
        mutableStateOf(false)
    }
    var itemIndex by remember {
        mutableStateOf(0)
    }

    val transform = ContentTransform(
        targetContentEnter = fadeIn(tween(durationMillis = 5000)),
        initialContentExit = fadeOut(tween(durationMillis = 5000))
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
                                    openDialogue = true
                                    itemIndex = index
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary

                                )
                            ) {


                                Text(text = "Get your Discount")

                                if (openDialogue) {

                                    if (CurrentUserHelper.isLoggedIn()) {
                                        val clipboardManager =
                                            LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip =
                                            ClipData.newPlainText(
                                                "coupon",
                                                couponsList[itemIndex]
                                            )
                                        clipboardManager.setPrimaryClip(clip)
                                        DiscountHelper.addDiscountValue(offers[itemIndex])


                                        WarningDialog(
                                            dialogTitle = "GREAT OFFERS !!",
                                            message = "Your have got ${offers[itemIndex]} % discount ,your coupon is  ${couponsList[itemIndex]}",
                                            dismissButtonText = "",
                                            confirmButtonText = "Copy Coupon Code",
                                            onConfirm = {
                                                openDialogue = false
                                            },
                                            onDismiss = {},
                                            addDismissButton = false
                                        )
                                    } else {

                                        WarningDialog(
                                            dialogTitle = "oops!!",
                                            message = "you are not signed in,please sign in to get your discount",
                                            dismissButtonText = "",
                                            confirmButtonText = "ok",
                                            onConfirm = {
                                                openDialogue = false
                                            },
                                            onDismiss = {},
                                            addDismissButton = false
                                        )
                                    }

                                }
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
    onClicked: () -> Unit
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
            onCheckedChange = { onClicked() }

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
    )
    {
        Text(
            text = stringResource(sectionTitle),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
            // .padding(vertical = 2.dp)
        )
        sectionContent()
    }
}

@Composable
fun BrandCards(
    modifier: Modifier = Modifier,
    brands: List<Brand>,
    navController: NavHostController
) {
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
    navController: NavHostController,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    products: List<ProductSample>,
    isFavourite: Boolean,
    onFavouriteClicked: (item: ProductSample) -> Unit,
    onAddToCard: (item: ProductSample) -> Unit
) {
    LazyRow(
        modifier = modifier.padding(start = 6.dp, end = 6.dp)
    ) {
        items(products) { item ->
            var isFavourite by remember {
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = Unit) {
                isFavourite = viewModel.isFavorite(item.id)
            }
            var openDialogue by remember {
                mutableStateOf(false)
            }
            CardDesign(onCardClicked = {}) {
                ItemCardContent(
                    navController = navController,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    isFavourite = isFavourite,
                    onFavouritesClicked = { product ->
                        if (CurrentUserHelper.isLoggedIn()) {

                            isFavourite = !isFavourite
                            if (isFavourite) {
                                viewModel.addWishlistItem(
                                    product.id,
                                    product.variants[0].id
                                )

                            }
                            if (!isFavourite) {
                                viewModel.removeWishlistItem(product.id)

                            }
                        } else {
                            openDialogue = true
                        }
                    },
                    onAddToCard = { onAddToCard(item) },
                    item = item
                )
            }
            if (openDialogue) {
                Surface(color = Color.Gray) {
                    ShowCustomDialog(
                        title = R.string.login,
                        description = R.string.please_login,
                        buttonText = R.string.login,
                        animatedId = R.raw.sign_for_error_or_explanation_alert,
                        buttonColor = MaterialTheme.colorScheme.error,
                        onClickButton = { navController.navigate(Screens.Login.route) },
                        onClose = { openDialogue = false }
                    )
                }
            }

        }
    }
}
