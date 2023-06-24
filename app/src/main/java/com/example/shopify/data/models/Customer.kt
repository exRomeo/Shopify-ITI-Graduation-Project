package com.example.shopify.data.models

import com.google.gson.annotations.SerializedName


data class CustomerRequestBody(
    val customer: Customer
)

data class CustomerResponseBody(
    val customer: SimpleResponse?,
)

data class Errors(
    val email: List<String?>?,
    val phone: List<String?>?
)

data class SimpleResponse(
    val id: Long,
    @field:SerializedName("first_name")
    val firstName: String?,
    @field:SerializedName("last_name")
    val lastName: String?,
    val email: String?,
    val phone: String?,
)

data class CustomerFirebase(
    val customer_id: Long?,
    val order_id: Long?,
    val card_id: Long?,
    val wishlist_id: Long?
) {
    constructor() : this(-1, -1, -1, -1)
}

data class Customer(
    @field:SerializedName("first_name")
    val firstName: String?=null,
    @field:SerializedName("last_name")
    val lastName: String?=null,
    val email: String,
    val phone: String? =null,
    @field:SerializedName("verified_email")
    val verifiedEmail: Boolean?,
    val addresses: List<Address>,
    val password: String?,
    @field:SerializedName("password_confirmation")
    val passwordConfirmation: String?,
    @field:SerializedName("send_email_welcome")
    val sendEmailWelcome: Boolean ? = null
)

data class Address(
    val address1: String,
    val city: String? = null,
    val province: String? = null,
    val phone: String,
    val zip: String? = null,
    val lastName: String? = null,
    val firstName: String? = null,
    val country: String? = null
)