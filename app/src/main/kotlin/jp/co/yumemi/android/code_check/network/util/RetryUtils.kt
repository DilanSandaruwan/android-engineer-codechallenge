package jp.co.yumemi.android.code_check.network.util

import android.util.Log
import jp.co.yumemi.android.code_check.constants.Constant.TAG
import kotlinx.coroutines.delay
import java.io.IOException

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
