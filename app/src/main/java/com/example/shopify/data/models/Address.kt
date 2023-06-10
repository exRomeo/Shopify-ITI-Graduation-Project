package com.example.shopify.data.models

data class Address(
    var id: Int,
    var phoneNumber: String,
    var firstName: String,
    var lastName: String,
    var fullAddress: String
) {
    fun getAddressString(): String = "$firstName $lastName\n$fullAddress"

}

