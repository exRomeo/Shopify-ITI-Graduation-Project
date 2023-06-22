package com.example.shopify.core.helpers

import android.content.Context
import android.content.SharedPreferences

object DiscountHelper{
    var sharedPreferences:SharedPreferences? = null
    var sharedPreferenceEditor: SharedPreferences.Editor? = null
    fun initialize(context:Context){
        sharedPreferences = context.getSharedPreferences("discounts",Context.MODE_PRIVATE)
        sharedPreferenceEditor = sharedPreferences?.edit()
    }



    fun addDiscountValue(discountvalue:Int){
        sharedPreferenceEditor?.putInt("discount", discountvalue)
        sharedPreferenceEditor?.apply()
        }

    fun getDiscount():Int? = sharedPreferences?.getInt("discount", 0)


}