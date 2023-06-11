package com.example.shopify.data.models

//data class Product(
//    val productId : Int?,
//    val productName :String?,
//    val productPrice : Double?,
//    val productImage :String?
//)

data class Products(
    val products:List<Varient>?,
):java.io.Serializable

data class Varient(
    val title:String?,
    val variants:List<Product>?,
    val image:Image?

)

data class Product(

    val product_id:Long?,
    val title:String?,
    val price:String?
)