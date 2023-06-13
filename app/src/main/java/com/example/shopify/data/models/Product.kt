package com.example.shopify.data.models

import java.io.Serializable

//data class Product(
//    val productId : Int?,
//    val productName :String?,
//    val productPrice : Double?,
//    val productImage :String?
//)

data class Products(
    val products:List<Variant>?,
): Serializable

data class Variant(
    val title:String?,
    val variants:List<Product>?,
    val image:Image?

)

data class Product(
    val product_id:Long?,
    val title:String?,
    val price:String?
)


data class ProductSample(
    var id: Int,
    val productName: String,
    val productPrice: Double,
    val imageURL: String,
    val orderDate: String,
    var amount: Int
)