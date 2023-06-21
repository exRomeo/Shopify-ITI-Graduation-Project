package com.example.shopify.core.utils

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.shopify.core.utils.SharedPreference.discountPercentage

@Composable
fun getDiscount(price:Double):Int{
    val sharedPreference = SharedPreference.customPreference(LocalContext.current,"customer")
    Log.i("getDiscount", sharedPreference.discountPercentage.toString())
    val discount = when (sharedPreference.discountPercentage){
        "5%" ->  5
        "20%" ->  20
        "50%" ->  50
        "70%" ->  70
         else -> 0
    }
    return discount
}