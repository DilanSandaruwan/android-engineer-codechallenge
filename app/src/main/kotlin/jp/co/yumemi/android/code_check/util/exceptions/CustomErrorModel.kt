package jp.co.yumemi.android.code_check.util.exceptions

/**
 * Data class representing a custom error model.
 *
 * @param majorErrorResId The resource ID of the major error message.
 * @param message An optional additional error message for more context.
 */
data class CustomErrorModel(
    val majorErrorResId: Int,
    val message: String?,
)