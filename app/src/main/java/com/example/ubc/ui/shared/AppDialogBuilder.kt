package com.example.ubc.ui.shared

import android.app.AlertDialog
import android.content.Context
import com.example.ubc.R

class AppDialogBuilder(context: Context?) : AlertDialog.Builder(context) {
    override fun create(): AlertDialog {
        val dialog = super.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_bg)
        return dialog
    }
}