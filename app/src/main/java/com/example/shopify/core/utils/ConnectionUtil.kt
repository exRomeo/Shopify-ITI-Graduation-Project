package com.example.shopify.core.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object ConnectionUtil {

    private var connectivityManager: ConnectivityManager? = null

    fun initialize(context: Context) {
        this.connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    }

    private var testMode = false
    fun setTestMode() {
        testMode = true
    }

    fun isConnected(): Boolean {
        if (!testMode) {
            val activeNetwork =
                connectivityManager?.getNetworkCapabilities(connectivityManager!!.activeNetwork)
                    ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else return true
    }
}


