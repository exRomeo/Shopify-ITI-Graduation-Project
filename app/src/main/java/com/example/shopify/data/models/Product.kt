package com.example.shopify.data.models

data class Product(
    var id: Int,
    val productName: String,
    val productPrice: Double,
    val imageURL: String,
    val orderDate: String,
    var amount: Int
)
