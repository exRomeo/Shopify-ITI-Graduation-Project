package com.example.shopify.data.repositories.checkout.local

import com.example.shopify.data.models.payment.CustomerApiModel
import com.example.shopify.data.models.payment.EphemeralKeyApiModel

class PaymentDao {
    private lateinit var customer: CustomerApiModel
    private lateinit var key: EphemeralKeyApiModel
    fun insertCustomer(customer: CustomerApiModel) {
        this.customer = customer

    }

    fun getCustomer(): CustomerApiModel = customer

    fun insertEphemeralKey(key: EphemeralKeyApiModel) {
        this.key = key
    }

}