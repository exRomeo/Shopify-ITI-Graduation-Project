package com.example.shopify.data.models.order

import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.LineItem
import com.google.gson.annotations.SerializedName

data class OrderBody(
    @field:SerializedName("order")
    val order: OrderOut
)

data class OrderResponse(
    @field:SerializedName("order")
    val order: OrderIn
)

data class OrderOut(
    @field:SerializedName("shipping_address")
    val shippingAddress: Address,
    @field:SerializedName("line_items")
    val lineItems: List<LineItem>,
    @field:SerializedName("discount_codes")
    val discountCodes: List<DiscountCode>? = null
)

data class DiscountCode(
    @field:SerializedName("code")
    val code: String,
    @field:SerializedName("amount")
    val amount: String,
    @field:SerializedName("type")
    val type: String
)

data class OrderIn(
    @field:SerializedName("id")
    val id: Long,
    @field:SerializedName("current_subtotal_price")
    val total: String,
    @field:SerializedName("total_discounts")
    val totalDiscounts: String,
    @field:SerializedName("order_status_url")
    val orderURL: String,
    @field:SerializedName("line_items")
    val lineItems: List<LineItem>,
    @field:SerializedName("created_at")
    val date: String,
    @field:SerializedName("currency")
    val currency: String,
    @field:SerializedName("confirmed")
    val confirmed: Boolean
) {
    fun getDateTime(): List<String> {
        return date.split("T")
    }
}

