package com.example.shopify.data.models.payment

import com.google.gson.annotations.SerializedName


data class CustomerApiModel(
    val id: String? = null,
    @field:SerializedName("object")
    val customerApiModelObject: String? = null,
    val created: Long? = null,
    val email: Any? = null,
    @field:SerializedName("invoice_prefix")
    val invoicePrefix: String? = null,
    val name: Any? = null,
    @field:SerializedName("next_invoice_sequence")
    val nextInvoiceSequence: Long? = null,
    @field:SerializedName("tax_exempt")
    val taxExempt: String? = null,
)

