package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName


data class SingleProductResponseBody(
    val product: SingleProduct
)

data class SingleProduct(
    val id: Long?,
    val title: String?,
    @field:SerializedName("body_html")
    val description: String?,
    val variants: List<Product>?,
    val images: List<Image>?
)