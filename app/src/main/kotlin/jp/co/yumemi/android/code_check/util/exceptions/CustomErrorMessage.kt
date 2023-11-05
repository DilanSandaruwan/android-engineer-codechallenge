package jp.co.yumemi.android.code_check.util.exceptions

import android.content.Context

/**
 * A utility object for creating custom error messages based on a CustomErrorModel and a context.
 */
object CustomErrorMessage {

    /**
     * Creates a custom error message by combining a major error message and an additional message, if available.
     *
     * @param customModel The CustomErrorModel containing the major error and additional message.
     * @param context The application context for accessing resources.
     * @return The formatted custom error message.
     */

    fun createMessage(customModel:CustomErrorModel,context: Context) : String{
        return if (customModel.message != null){
            if (customModel.majorErrorResId > 0){
                context.resources.getString(customModel.majorErrorResId) + customModel.message
            } else {
                customModel.message
            }
        } else {
            if (customModel.majorErrorResId > 0){
                context.resources.getString(customModel.majorErrorResId)
            } else {
                ""
            }
        }
    }
}