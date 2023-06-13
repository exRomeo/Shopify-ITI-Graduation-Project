package com.example.shopify.data.models.draftorder

data class DraftOrderBody(
    val draftOrder: DraftOrder
)

data class DraftOrderResponse(
    val draftOrder: DraftOrder
)

data class LineItem(
    val id: Long,
    var variantID: Long? = null,
    var productID: Long? = null,
    val title: String,
    val quantity: Long,
    val name: String,
    val price: String
)

data class DraftOrder(
    val id: Long,
    val note: String,
    val email: String,
    val currency: String,
    val name: String,
    val status: String,
    val lineItems: List<LineItem>,
    val totalPrice: String,
    val customer: Customer
)

data class Customer(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String
)