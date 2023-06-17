package com.example.shopify.data.models

import java.io.Serializable


data class Products(
    val products:List<ProductSample>?,
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
    val price: String?
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
