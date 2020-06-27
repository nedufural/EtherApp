package com.fastcon.etherapp.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull


class Extensions {
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

        /**
         *  Show softKeyboard.
         * @param context
         */

        fun showKeyBoard(@NonNull context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        /**
         * Hide softKeyboard.
         *
         * @param activity
         * @param editText
         */
        fun hideKeyBoard(activity: Activity, editText: EditText) {
            if ((activity.currentFocus == null) || activity.currentFocus !is EditText) {
                editText.requestFocus()
            }
            val imm =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
            activity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }
}