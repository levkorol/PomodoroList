package ru.harlion.pomodorolist.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import ru.harlion.pomodorolist.R

class AlertDialogBase(context: Context)  {

    private var alertDialog: Dialog = Dialog(context)

    init {
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog.setCancelable(false)
        alertDialog.setContentView(R.layout.fragment_alert_dialog)
        alertDialog.window!!.apply {

          val inset = context.resources.getDimensionPixelSize(R.dimen.dialog_inset_50dp_25_dp)
            setBackgroundDrawable(
                InsetDrawable(
                AppCompatResources.getDrawable(context, R.drawable.bg_white),
                inset,
                0,
                inset,
                0
            )
            )
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            decorView.apply {
                layoutParams =
                    ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
              setPadding(inset, 0, inset, 0)
            }
        }
    }


    fun setTitle(msg: String) {
      alertDialog.findViewById<TextView>(R.id.title_dialog).apply {
          text = msg
          visibility = View.VISIBLE
      }
    }

    fun setEditText(hintText: String, editText: String): String {
       alertDialog.findViewById<EditText>(R.id.editText).apply {
           hint = hintText
           text = editText as Editable //todo
           visibility = View.VISIBLE
       }
        return editText
    }

    fun setPositiveButton(title: String, onClickListener: View.OnClickListener?) {
        alertDialog.findViewById<AppCompatButton>(R.id.positive_button).apply {
            text = title
            visibility = View.VISIBLE
            setOnClickListener {
                onClickListener?.onClick(it)
                alertDialog.dismiss()
            }
        }
    }

    fun setNegativeButton(title: String, onClickListener: View.OnClickListener?) {
        alertDialog.findViewById<AppCompatButton>(R.id.negative_button).apply {
            text = title
            visibility = View.VISIBLE
            setOnClickListener {
                onClickListener?.onClick(it)
                alertDialog.dismiss()
            }
        }
    }

    fun show() {
        alertDialog.show()
    }

    fun cancelable(isCancelable: Boolean) {
        alertDialog.setCancelable(isCancelable)
    }
}