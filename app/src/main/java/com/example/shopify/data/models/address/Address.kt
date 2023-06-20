package com.example.shopify.data.models.address

import com.example.shopify.data.models.ItemWithName
import com.google.gson.annotations.SerializedName

data class Address(
    @field:SerializedName("id")
    var id: Long = 0,
    @field:SerializedName("customer_id")
    var customerID: Long = 0,
    @field:SerializedName("phone")
    var phoneNumber: String,
    @field:SerializedName("first_name")
    var firstName: String,
    @field:SerializedName("last_name")
    var lastName: String,
    @field:SerializedName("address1")
    var fullAddress: String,
    @field:SerializedName("default")
    var default: Boolean = false
) : ItemWithName {
    fun getAddressString(): String = "$firstName $lastName\n$fullAddress"
    override fun getShortName(): String = fullAddress

    override fun getFullName(): String = "$firstName $lastName\n $fullAddress\n$phoneNumber"

    override fun getName(): String = "$firstName $lastName"

}

data class AddressBody(
    @field:SerializedName("address")
    var address: Address
)

data class AddressesResponse(
    @field:SerializedName("addresses")
    var addresses: List<Address>
)

data class NewAddressResponse(
    @field:SerializedName("customer_address")
    var address: Address
)

class DeleteResponse


