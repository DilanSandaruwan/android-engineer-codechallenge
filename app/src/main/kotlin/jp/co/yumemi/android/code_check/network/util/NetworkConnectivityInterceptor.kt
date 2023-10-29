package jp.co.yumemi.android.code_check.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.util.exceptions.NetworkUnavailableException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectivityInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NetworkUnavailableException(context.getString(R.string.no_network_found))
        }
        return chain.proceed(chain.request())
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Check if the device is running Android 29 (Android 10) or newer
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For devices running Android versions 29 or higher (e.g., Android 10 or newer)
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

            networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            // For devices running Android versions prior to 29 (e.g., Android 9 or earlier)
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}