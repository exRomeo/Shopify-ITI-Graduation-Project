package com.example.shopify.data.models

interface ItemWithName {
    fun getShortName(): String
    fun getFullName(): String
    fun getName(): String
}