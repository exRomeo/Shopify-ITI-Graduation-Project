package com.example.shopify.data.models.currency

data class CurrencyResponse (
    val success: Boolean? = null,
    val query: Query? = null,
    val info: Info? = null,
    val date: String? = null,
    val result: Double? = null
)

data class Info (
    val timestamp: Long? = null,
    val rate: Double? = null
)

data class Query (
    val from: String? = null,
    val to: String? = null,
    val amount: Long? = null
)
