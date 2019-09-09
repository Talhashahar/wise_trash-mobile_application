package com.msl.talfinalproject.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Window
import android.widget.RadioGroup
import android.widget.RelativeLayout
import com.msl.talfinalproject.R
import com.msl.talfinalproject.ui.FontableTextView
import kotlinx.android.synthetic.main.dialog_trash_event_layout.*

class AlertDialog {
    companion object {


        fun createDialog(context: Context) : Dialog {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("do you wish to sign out?")
            builder.setTitle("Sign Out")

            builder.setPositiveButton("ok") { dialog, id -> }

            builder.setNegativeButton("Cancel") { dialog, id -> }
            return builder.create()
        }

        fun createWrongInputDialog(context: Context, errorMsg : String, dismiss: DialogInterface.OnDismissListener? = null) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_wrong_input_layout)
            dialog.window!!.setBackgroundDrawable(context.getDrawable(R.drawable.dialog_wrong_input_bg))


            // set the custom dialog components - text, image and button
            val text = dialog.findViewById(R.id.error_message) as FontableTextView
            text.setText(errorMsg)

            val dialogButton = dialog.findViewById(R.id.button_dismiss) as RelativeLayout
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener { dialog.dismiss() }

            if(dismiss != null)
                dialog.setOnDismissListener(dismiss)

            dialog.show()
        }

        fun createTrashEventDialog(context: Context,
                                   onChecked : RadioGroup.OnCheckedChangeListener,
                                   dismiss: DialogInterface.OnDismissListener? = null) {
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(R.layout.dialog_trash_event_layout)
            dialog.window!!.setBackgroundDrawable(context.getDrawable(R.drawable.dialog_wrong_input_bg))


            dialog.radio_group.setOnCheckedChangeListener(onChecked)


            val dialogButton = dialog.findViewById(R.id.button_dismiss) as RelativeLayout
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener { dialog.dismiss() }

            if(dismiss != null)
                dialog.setOnDismissListener(dismiss)

            dialog.show()
        }
    }
}