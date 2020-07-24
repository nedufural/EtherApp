package com.fastcon.etherapp.util.functions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkUtil {
    companion object {
        /**
         * Check network connectivity
         * @param context
         * */
        fun checkInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            // for API level below 29
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                val networkInfo = connectivityManager.activeNetworkInfo
                return networkInfo != null && networkInfo.isConnected
            }

            // for API level 29 and above
            val networks = connectivityManager.allNetworks
            var hasInternet = false
            if (networks.isNotEmpty()) {
                for (network in networks) {
                    val nc = connectivityManager.getNetworkCapabilities(network)
                    if (nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) hasInternet =
                        true
                }
            }
            return hasInternet
        }

    }
}