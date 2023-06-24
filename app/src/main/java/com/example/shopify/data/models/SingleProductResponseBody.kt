package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName


data class SingleProductResponseBody(
    val product: SingleProduct? = null
)

data class SingleProduct(
    val id: Long?,
    val title: String?,
    @field:SerializedName("body_html")
    val description: String?,
    val variants: List<Product>?,
    val options: List<Options>?,
    val images: List<Image>?
)

data class Options(
    val id: Long?,
    @field:SerializedName("product_id")
    val productID: Long?,
    val name: String?,
    val position: Int?,
    val values: List<String>?,
)