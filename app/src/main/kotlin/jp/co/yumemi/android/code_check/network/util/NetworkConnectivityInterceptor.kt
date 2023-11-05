package jp.co.yumemi.android.code_check.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.constants.Constant.TEST_URL
import jp.co.yumemi.android.code_check.util.exceptions.NetworkUnavailableException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * An interceptor for handling network connectivity checks in Retrofit.
 *
 * This interceptor checks for the availability of network connectivity and internet reachability
 * before making an API request. It throws a [NetworkUnavailableException] with an appropriate error
 * message if network conditions are not met.
 *
 * @param context The application context for accessing system services.
 */
class NetworkConnectivityInterceptor(private val context: Context) : Interceptor {

    /**
     * Intercepts the request chain and checks for network availability and internet reachability.
     *
     * @param chain The intercepted request chain.
     * @return The response from the chain if network conditions are met.
     * @throws NetworkUnavailableException if network conditions are not met.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NetworkUnavailableException(context.getString(R.string.no_network_found))
        } else {
            if (!isInternetReachable()) {
                throw NetworkUnavailableException(context.getString(R.string.network_unreachable))
            }
        }
        return chain.proceed(chain.request())
    }

    /**
     * Checks if network connectivity is available.
     *
     * @return `true` if network connectivity is available, `false` otherwise.
     */
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

    /**
     * Checks if internet is reachable by attempting to make a simple connection to a test URL.
     *
     * @return `true` if internet is reachable, `false` otherwise.
     */
    private fun isInternetReachable(): Boolean {
        return try {
            val urlConnection = URL(TEST_URL).openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("User-Agent", "test")
            urlConnection.setRequestProperty("Connection", "close")
            urlConnection.connectTimeout = 1000 // Set a timeout in milliseconds

            // Check if the connection was successful
            val responseCode = urlConnection.responseCode
            responseCode == 200
        } catch (e: IOException) {
            // An error occurred, indicating no internet access
            false
        }
    }

}