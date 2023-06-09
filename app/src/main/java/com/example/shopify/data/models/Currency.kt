package com.example.shopify.data.models

data class Currency(val symbol: String, val country: String) {
    companion object {
        val list = listOf(
            Currency("AED", "United Arab Emirates Dirham"),
            Currency("BHD", "Bahraini Dinar"),
            Currency("CNY", "Chinese Yuan"),
            Currency("EGP", "Egyptian Pound"),
            Currency("EUR", "Euro"),
            Currency("GBP", "British Pound Sterling"),
            Currency("IQD", "Iraqi Dinar"),
            Currency("JOD", "Jordanian Dinar"),
            Currency("KWD", "Kuwaiti Dinar"),
            Currency("LBP", "Lebanese Pound"),
            Currency("LYD", "Libyan Dinar"),
            Currency("MAD", "Moroccan Dirham"),
            Currency("NZD", "New Zealand Dollar"),
            Currency("OMR", "Omani Rial"),
            Currency("QAR", "Qatari Rial"),
            Currency("SAR", "Saudi Riyal"),
            Currency("SDG", "Sudanese Pound"),
            Currency("SYP", "Syrian Pound"),
            Currency("TND", "Tunisian Dinar"),
            Currency("TRY", "Turkish Lira"),
            Currency("UAH", "Ukrainian Hryvnia"),
            Currency("USD", "United States Dollar"),
            Currency("YER", "Yemeni Rial"),
            Currency("ZAR", "South African Rand")
        )
    }
}
