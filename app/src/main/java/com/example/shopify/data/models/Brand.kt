package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class SmartCollections(
    val smart_collections: List<Brand>? =null
) : Serializable

data class Brand(
    val id:Long,
    @SerializedName("title")
    val name: String?,
    val image: Image?
) : Serializable

data class Image(
//    val position: Int?,
    val src: String?
) : Serializable