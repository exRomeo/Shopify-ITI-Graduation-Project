package com.example.shopify.core.utils

class Constant {
    companion object {
        val EMAIL_PATTERN = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        val PASSWORD_PATTERN =
            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{8,}$")
       // val PHONE_PATTERN = Regex("^\\+(?:[0-9] ?){6,14}[0-9]\$") // This regex matches international phone numbers

        val PHONE_PATTERN = Regex("^\\+?[0-9]{7,16}\$")
    }
}
