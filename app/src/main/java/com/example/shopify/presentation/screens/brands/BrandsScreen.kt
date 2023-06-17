package com.example.shopify.presentation.screens.brands


import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Product

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.Variant
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.screens.homescreen.CardDesign
import com.example.shopify.presentation.screens.homescreen.FavoriteButton
import com.example.shopify.presentation.screens.homescreen.ImageFromNetwork
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModel
import com.example.shopify.presentation.screens.settingsscreen.SettingsViewModelFactory


@SuppressLint("SuspiciousIndentation")
@Composable
fun BrandsScreen(navController: NavHostController, id: Long?) {
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    // val repository2: I = (LocalContext.current.applicationContext as ShopifyApplication) .repository
    val viewModel: BrandsViewModel = viewModel(
        factory = BrandsViewModelFactory(
            repository
        )
    )

    val productsState: UiState by viewModel.brandList.collectAsState()
    var productsList: List<Variant> = listOf()

    LaunchedEffect(Unit) {

        if (id != null) {
            viewModel.id = id
            //Log.i("menna", id.toString())
            viewModel.getSpecificBrandProducts()
        }

    }
    when (productsState) {
        is UiState.Loading -> {

            LottieAnimation(animation = R.raw.loading_animation)

        }

        is UiState.Success<*> -> {
            // Log.i("menna", "success")
            productsList =
                (productsState as UiState.Success<Products>).data.body()?.products!!
            Log.i("menna", productsList.toString())
        }

        else -> {
            Log.i("homepage", (productsState as UiState.Error).error.toString())
        }
    }
    if (productsList.isNotEmpty()) {

        ProductsCards(
            navController = navController,
            // viewModel= settingsViewModel,
            modifier = Modifier,
            isFavourite = true,
            onFavouriteClicked = {},
            onAddToCard = {},
            products = productsList,
        )

    }
}


@Composable
fun ProductsCards(
    navController: NavHostController,
    //viewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    products: List<Variant>,
    isFavourite: Boolean,
    onFavouriteClicked: (Boolean) -> Unit,
    onAddToCard: (item: Product) -> Unit
) {
    LazyColumn(
        modifier = modifier,

        // content padding
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        content = {
            items(products) { item ->
                CardDesign(onCardClicked = {
                    navController.navigate(route = "${Screens.Details.route}/${item.id}")
                }) {
                    ProductItem(isFavourite = isFavourite, onFavouritesClicked = {
                        var productSample: ProductSample =
                            ProductSample(
                                id = item.id,
                                title = item.title!!,
                                variants = item.variants!!,
                                images = listOf(item.image)!! as List<Image>,
                                image = item.image!!
                            )

                        //   viewModel.addWishlistItem(productSample)


                    }, onAddToCard = onAddToCard, item = item, navController = navController)

                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(
    navController: NavHostController,
    modifier: Modifier = Modifier, isFavourite: Boolean,
    onFavouritesClicked: (Boolean) -> Unit, onAddToCard: (item: Product) -> Unit, item: Variant
) {
    Card(onClick = {
        navController.navigate(route = "${Screens.Details.route}/${item.id}")
    }, modifier = Modifier.height(200.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            item.image?.src?.let {
                ImageFromNetwork(
                    image = it,
                    modifier = Modifier
                        //.fillMaxHeight()
                        // .clip(RoundedCornerShape(15.dp))
                        .height(200.dp)
                        .background(color = Color.White)
                    //.align(Alignment.CenterHorizontally)

                )
            }
            Column() {
                item.title.let {
                    if (it != null) {
                        Text(
                            modifier = Modifier.padding(top = 10.dp),
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {


                    item.variants?.get(0)?.price.let {
                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    FavoriteButton(isFavourite = isFavourite, onClicked = onFavouritesClicked)
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

    }


}

@Preview
@Composable
fun ProductCardPreview() {
//    ProductsCards(
//    isFavourite = true,
//    onFavouriteClicked = {},
//    onAddToCard = {},
//        products = list,
//    )
}

