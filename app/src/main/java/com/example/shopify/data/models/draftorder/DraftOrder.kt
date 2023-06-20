package com.example.shopify.data.models.draftorder

import com.google.gson.annotations.SerializedName

data class DraftOrderBody(
    @field:SerializedName("draft_order")
    val draftOrder: DraftOrder
)

data class LineItem(
    @field:SerializedName("variant_id")
    var variantID: Long,
    @field:SerializedName("product_id")
    var productID: Long,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("quantity")
    var quantity: Long,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("price")
    val price: String
) {
    fun getTotalPrice(): String = (price.toDouble() * quantity).toString()
}

data class DraftOrder(
    @field:SerializedName("id")
    val id: Long,
    @field:SerializedName("note")
    val note: String,
    @field:SerializedName("line_items")
    var lineItems: MutableList<LineItem>,
    @field:SerializedName("total_price")
    val totalPrice: String
)

