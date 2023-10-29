package jp.co.yumemi.android.code_check.util.components

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class CustomDialogFragment : DialogFragment() {
    private var title: String = "Dialog Title"
    private var message: String = "Dialog Message"
    private var positiveText: String = "OK"
    private var negativeText: String = "Cancel"
    private var positiveClickListener: (() -> Unit)? = null
    private var negativeClickListener: (() -> Unit)? = null
    private var iconResId: Int? = null

    companion object {
        fun newInstance(
            title: String,
            message: String,
            positiveText: String = "OK",
            negativeText: String = "Cancel",
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
        return builder.create()
    }
}