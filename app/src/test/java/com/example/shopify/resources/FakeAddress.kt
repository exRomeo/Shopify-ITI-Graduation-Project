package com.example.shopify.resources

import com.example.shopify.data.models.address.Address

object FakeAddress {
    val address = Address(
        id = 5,
        customerID = 312,
        phoneNumber = "5678901234",
        firstName = "Doe",
        lastName = "John",
        fullAddress = "123 Main Street, City, Country",
        default = true
    )

    val address1 = Address(
        id = 1,
        customerID = 123,
        phoneNumber = "1234567890",
        firstName = "John",
        lastName = "Doe",
        fullAddress = "123 Main Street",
        default = true
    )

    val address2 = Address(
        id = 2,
        customerID = 456,
        phoneNumber = "9876543210",
        firstName = "Jane",
        lastName = "Smith",
        fullAddress = "456 Elm Street",
        default = false
    )

    val address3 = Address(
        id = 3,
        customerID = 789,
        phoneNumber = "5555555555",
        firstName = "Alice",
        lastName = "Johnson",
        fullAddress = "789 Oak Street",
        default = false
    )

    val addressList = mutableListOf(address1, address2, address3)

}