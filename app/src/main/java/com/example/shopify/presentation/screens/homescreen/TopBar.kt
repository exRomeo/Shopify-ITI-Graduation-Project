package com.example.shopify.presentation.screens.homescreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.example.shopify.ui.theme.lightMainColor
import com.example.shopify.ui.theme.mainColor
import com.example.shopify.ui.theme.onMainColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    onCartClick: () -> Unit,
    onWishlistClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = lightMainColor
        ),
        title = {
            Text(
                text = title,
                color = mainColor
            )


        },
        actions = {
            //navigate to carts
            IconButton(onClick = onCartClick) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart, contentDescription = "shopping cart",
                    tint = onMainColor
                )
            }
            //navigate to favourites
            IconButton(onClick = onWishlistClick) {
                Icon(
                    imageVector = Icons.Outlined.Favorite, contentDescription = "Favourite",
                    tint = onMainColor
                )
            }

        }
    )
}