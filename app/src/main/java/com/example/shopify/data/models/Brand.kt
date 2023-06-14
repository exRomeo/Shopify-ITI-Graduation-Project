package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName


data class SmartCollections(
    val smart_collections:List<Brand>
    ) :java.io.Serializable
data class Brand(
    val id:Long,
    @SerializedName("title")
    val name:String?,
    val image:Image?
):java.io.Serializable

data class Image(
    val src :String?
):java.io.Serializable