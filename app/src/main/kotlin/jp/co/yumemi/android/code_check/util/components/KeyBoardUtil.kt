package jp.co.yumemi.android.code_check.util.components

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyBoardUtil {
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}