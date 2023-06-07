package com.example.shopify.presentation.screens.homescreen

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.shopify.R
import com.example.shopify.data.models.Product


@Composable
fun HomeScreen(viewModel: HomeViewModel) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDesign(modifier: Modifier = Modifier,onCardClicked:()->Unit,content:@Composable () -> Unit ) {
    Card(modifier = modifier.padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
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
fun ItemCardContent(modifier: Modifier = Modifier,isFavourite:Boolean,
                    onClicked:(Boolean)->Unit,item:Product){
    Column (modifier = Modifier.padding(15.dp)){
            item.productName?.let { ImageFromNetwork(Modifier, it) }

        Divider(thickness = 5.dp, color= Color.Black, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(30.dp)){
            Text(
                text = "shoes",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            FavoriteButton(isFavourite = isFavourite , onClicked = onClicked )
        }
        Text(text = "price",
            style =MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.secondary)
    }


}

@Composable
fun BrandCardContent(modifier: Modifier= Modifier,brand:Product){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(15.dp),
    ) {
        brand.productName?.let { ImageFromNetwork(modifier = Modifier.align(Alignment.CenterHorizontally),image = it) }


        brand.productName?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

    }

}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
 fun adsCarousel(onAddClick:()->Unit){
    val items:List<String> = listOf("https://www.seiu1000.org/sites/main/files/main-images/camera_lense_0.jpeg",
        "https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Adidas_Logo.svg/2560px-Adidas_Logo.svg.png")

    val transform = ContentTransform(
        targetContentEnter = fadeIn(tween(durationMillis = 1000)),
        initialContentExit = fadeOut(tween(durationMillis = 1000))
    )

    Carousel(
        contentTransformForward = transform,
        contentTransformBackward = transform,
        slideCount = items.count() ,
        modifier = Modifier.fillMaxWidth().padding(16.dp).clickable { onAddClick }
                ,
        content = { index ->
            items[index].also { item ->
                CarouselItem(
                    background = {
                        AsyncImage(
                            model =items[index], contentDescription = null, contentScale = ContentScale.Inside,
                           // modifier = Modifier.height(200.dp).width(800.dp)
                        )
                    },
                content = {

                }
                )
            }

            })
        }




@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavourite:Boolean,
    onClicked:(Boolean)->Unit
) {
    Surface(
        shape = CircleShape,
        modifier = modifier
            .padding(6.dp)
            .size(32.dp),
        color = Color.White
    ) {
            IconToggleButton(
                checked = isFavourite,
                onCheckedChange = onClicked

            ) {
                Icon(
                    tint = MaterialTheme.colorScheme.surfaceTint,
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
fun ImageFromNetwork(modifier: Modifier = Modifier,image:String){
    val painter = rememberAsyncImagePainter(
        model = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/20/Adidas_Logo.svg/2560px-Adidas_Logo.svg.png",
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
fun HomeSection(modifier: Modifier = Modifier,@StringRes sectionTitle:Int,sectionContent: @Composable ()->Unit){
    Column(verticalArrangement = Arrangement.spacedBy(10.dp),
    modifier = modifier)
    {
        Text(text = stringResource(sectionTitle),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding( 8.dp))
        sectionContent()
    }
}

@Composable
fun BrandCards(modifier: Modifier = Modifier,brands:List<Product>){
    LazyRow(
        modifier = modifier
    ) {
        items(brands) { item ->
//            var isf by remember {
//                mutableStateOf(false)}
          CardDesign(onCardClicked = {}) {
             BrandCardContent(brand = item)

          }

          }
        }
}
@Composable
fun ItemCards(modifier: Modifier = Modifier,brands:List<Product>){
    LazyRow(
        modifier = modifier.padding(start = 6.dp, end = 6.dp)
    ) {
        items(brands) { item ->
          var isf by remember {
                mutableStateOf(false)}
            CardDesign(onCardClicked = {}) {
                ItemCardContent(isFavourite = isf, onClicked ={isf -> isf != isf} , item = item )
            }

        }
    }
}


@Preview
@Composable
fun BrandCardDesignPreview() {
    var isf by remember {

    mutableStateOf(false)}
//    ItemCards(brands = listOf(Product(1, "menna", 100.0),Product(1,"menna",100.0)
//        ,Product(1,"menna",100.0)
//        ,Product(1,"menna",100.0)
//        ,Product(1,"menna",100.0)
//        ,Product(1,"menna",100.0)
//        ,Product(1,"menna",100.0)
//        ,Product(1,"menna",100.0)))

    //adsCarousel()
  //CardDesign(Modifier,{}, { ItemCardContent(isFavourite = false, onClicked ={isf = !isf} ) })
   // BrandCardDesign(Modifier,{}, {BrandCardContent()})
   // BrandCards(brands = (listOf (Product(1,"menna",100.0)



}


