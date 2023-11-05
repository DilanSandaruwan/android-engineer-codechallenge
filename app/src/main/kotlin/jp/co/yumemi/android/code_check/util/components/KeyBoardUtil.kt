package jp.co.yumemi.android.code_check.util.components

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * A utility object for managing the visibility of the software keyboard.
 */
object KeyBoardUtil {

    /**
     * Hides the software keyboard associated with a given view.
     *
     * @param context The application context.
     * @param view The view that has focus and is currently displaying the keyboard.
     */
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}