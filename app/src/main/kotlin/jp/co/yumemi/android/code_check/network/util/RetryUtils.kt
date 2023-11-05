package jp.co.yumemi.android.code_check.network.util

import android.util.Log
import jp.co.yumemi.android.code_check.constants.Constant.TAG
import kotlinx.coroutines.delay
import java.io.IOException

/**
 * A utility function for retrying an I/O operation with a specified number of attempts and delays between retries.
 *
 * @param times The maximum number of retry attempts.
 * @param initialDelayMillis The initial delay in milliseconds before the first retry attempt.
 * @param maxDelayMillis The maximum delay in milliseconds between retries.
 * @param block A suspend function representing the I/O operation to be retried.
 * @return The result of the I/O operation if successful after retries.
 * @throws IOException if the I/O operation fails after all retry attempts.
 */
suspend fun <T> retryIOOperation(
    times: Int,
    initialDelayMillis: Long = 1000,
    maxDelayMillis: Long = 60000,
    block: suspend () -> T
): T {
    var currentDelay = initialDelayMillis
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            Log.e("$TAG - Retry Check", "Network error: ${e.message}")
        }
        delay(currentDelay)
        currentDelay = (currentDelay * 2).coerceAtMost(maxDelayMillis)
    }
    return block() // Last attempt, propagate any exception
}
