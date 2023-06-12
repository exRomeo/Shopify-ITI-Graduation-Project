package com.example.shopify.data.models

data class RequestBody(
    val customer: Customer
)

data class ResponseBody(
    val customer: Response?,
    val errors : Errors?
)
data class Errors(
    val email: List<String?>?,
    val phone: List<String?>?
)
data class Response(
    val id: Long,
    val customerId : String?
)
data class Customer(
    val first_name: String,
    val last_name: String,
    val email: String,
    val phone: String,
    val verified_email: Boolean,
    val addresses: List<Address>,
    val password: String,
    val password_confirmation: String,
    val send_email_welcome: Boolean
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