package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("title")
    val name:String?,
    val image:Image?
):java.io.Serializable

data class Image(
    val src :String?
):java.io.Serializable