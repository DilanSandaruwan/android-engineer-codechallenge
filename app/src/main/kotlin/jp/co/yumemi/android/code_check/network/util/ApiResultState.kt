package jp.co.yumemi.android.code_check.network.util

/**
 * A sealed class representing the result state of an API operation. It can either be a success or a failure.
 *
 * @param T The type of data associated with the success state.
 */
sealed class ApiResultState<out T> {
    /**
     * Represents a successful API operation with associated data.
     *
     * @property data The data associated with the success state.
     */
    data class Success<out T>(val data: T) : ApiResultState<T>()

    /**
     * Represents a failed API operation.
     *
     * @property majorErrorResId The resource ID of a major error, if applicable.
     * @property message An optional error message providing additional information about the failure.
     */
    data class Failed(val majorErrorResId:Int, val message: String?) : ApiResultState<Nothing>()
}
