package jp.co.yumemi.android.code_check.network.util

sealed class ApiResultState<out T> {
    data class Success<out T>(val data: T) : ApiResultState<T>()
    data class Failed(val msg: String) : ApiResultState<Nothing>()
}
