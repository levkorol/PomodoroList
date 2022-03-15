package ru.harlion.pomodorolist.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View

class AlertDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    fun setTitle(msg: String) {

    }

    fun setPositiveButton(title: String, onClickListener: View.OnClickListener?) {

    }

    fun setNegativeButton(title: String, onClickListener: View.OnClickListener?) {

    }

}