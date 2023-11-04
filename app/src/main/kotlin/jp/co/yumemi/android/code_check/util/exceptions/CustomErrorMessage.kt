package jp.co.yumemi.android.code_check.util.exceptions

import android.content.Context

object CustomErrorMessage {
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