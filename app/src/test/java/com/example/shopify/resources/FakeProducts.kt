package com.example.shopify.resources

import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.ProductSample

object FakeProducts {
    private val variant1 = Product(1, 55555555564,"Variant 1","50",10,"")
    private val variant2 = Product(2,124534536 ,"Variant 2","510",3,"")
    private val variant3 = Product(3,1234657 ,"Variant 3","510",3,"")

    private val image1 = Image("image1.jpg")
    private val image2 = Image("image2.jpg")
    private val image3 = Image("image3.jpg")

    val product1 = ProductSample(1, "Product 1", listOf(variant1, variant2), listOf(image1, image2), image1)
    val product2 = ProductSample(2, "Product 2", listOf(variant2, variant3), listOf(image2, image3), image2)
    val product3 = ProductSample(3, "Product 3", listOf(variant1, variant3), listOf(image1, image3), image3)
    val product4 = ProductSample(4, "Product 4", listOf(variant2, variant1), listOf(image2, image3), image1)

    val productList = mutableListOf(product1, product2, product3)
}