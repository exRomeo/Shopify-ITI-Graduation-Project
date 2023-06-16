package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName

data class Products(
    val products: List<Varient>?,
) : java.io.Serializable

data class Varient(
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
    val availableAmount: Long?
)


data class ProductSample(
    var id: Long,
    val title: String,
    val variants: List<Product>,
    val images: List<Image>,
    val image: Image
)

data class ProductResponse(
    val product: ProductSample
)