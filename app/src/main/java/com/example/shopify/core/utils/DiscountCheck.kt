package com.example.shopify.core.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.shopify.core.utils.SharedPreference.discountPercentage

@Composable
fun applyDiscount(price:Double):Double{
    val sharedPreference = SharedPreference.customPreference(LocalContext.current,"customer")
    Log.i("sna", sharedPreference.discountPercentage.toString())
    val totalPrice = when (sharedPreference.discountPercentage){
        "5%" ->  price - price * 0.05
         "20%" ->  price - price * 0.2
        "50%" ->  price - price * 0.5
        "70%" ->  price - price * 0.7
         else ->
             price

    }
    Log.i("sna", totalPrice.toString())
    return totalPrice
}