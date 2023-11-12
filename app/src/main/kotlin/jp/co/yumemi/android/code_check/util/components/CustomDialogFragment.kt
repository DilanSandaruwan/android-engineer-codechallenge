package jp.co.yumemi.android.code_check.util.components

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import jp.co.yumemi.android.code_check.constants.TagConstant

/**
 * A custom dialog fragment for displaying a simple dialog with customizable content and actions.
 */
class CustomDialogFragment : DialogFragment() {
    private var title: String = TagConstant.DIALOG_FRAGMENT_TITLE
    private var message: String = TagConstant.DIALOG_FRAGMENT_MESSAGE
    private var positiveText: String = TagConstant.DIALOG_FRAGMENT_OK
    private var negativeText: String = TagConstant.DIALOG_FRAGMENT_CANCEL
    private var positiveClickListener: (() -> Unit)? = null
    private var negativeClickListener: (() -> Unit)? = null
    private var iconResId: Int? = null

    /**
     * A companion object providing a convenient way to create instances of CustomDialogFragment with customizable parameters.
     */
    companion object {
        /**
         * Create a new instance of CustomDialogFragment with customizable parameters.
         *
         * @param title The title of the dialog.
         * @param message The message content of the dialog.
         * @param positiveText The text for the positive button (e.g., OK).
         * @param negativeText The text for the negative button (e.g., Cancel).
         * @param positiveClickListener A lambda function to be executed when the positive button is clicked.
         * @param negativeClickListener A lambda function to be executed when the negative button is clicked.
         * @param iconResId An optional resource ID for an icon to be displayed in the dialog.
         * @return An instance of CustomDialogFragment with the specified parameters.
         */
        fun newInstance(
            title: String,
            message: String,
            positiveText: String = TagConstant.DIALOG_FRAGMENT_OK,
            negativeText: String = TagConstant.DIALOG_FRAGMENT_CANCEL,
            positiveClickListener: (() -> Unit)? = null,
            negativeClickListener: (() -> Unit)? = null,
            iconResId: Int? = null
        ): CustomDialogFragment {
            return CustomDialogFragment().apply {
                this.title = title
                this.message = message
                this.positiveText = positiveText
                this.negativeText = negativeText
                this.positiveClickListener = positiveClickListener
                this.negativeClickListener = negativeClickListener
                this.iconResId = iconResId
            }
        }
    }

    /**
     * Creates and configures the dialog when the fragment is being created.
     *
     * @param savedInstanceState The saved instance state, if any.
     * @return A dialog displaying the specified title, message, buttons, and icon.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText) { _, _ ->
            positiveClickListener?.invoke()
        }
        builder.setNegativeButton(negativeText) { _, _ ->
            negativeClickListener?.invoke()
        }
        iconResId?.let {
            builder.setIcon(it)
        }
        isCancelable = false
        return builder.create()
    }
}