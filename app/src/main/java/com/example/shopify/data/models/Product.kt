package com.example.shopify.data.models

import java.io.Serializable
import com.google.gson.annotations.SerializedName

data class Products(
    val products:List<ProductSample>? = null,
): Serializable

data class Variant(
    var id: Long,
    val title: String?,
    val variants: List<Product>?,
    val image: Image?
)


data class Product(
    val id: Long,
    val product_id: Long?,
    val title: String?,
    val price: String?,
    @field:SerializedName("inventory_quantity")
    val availableAmount: Long?,
    @field:SerializedName("option1")
    val size: String? = ""
)


data class ProductSample(
    var id: Long,
    val title: String,
    val variants: List<Product>,
    val images: List<Image>,
    val image: Image,
    val isFavorite: Boolean = false
)

data class ProductResponse(
    val product: ProductSample
)