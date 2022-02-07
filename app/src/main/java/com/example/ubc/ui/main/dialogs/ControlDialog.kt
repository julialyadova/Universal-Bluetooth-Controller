package com.example.ubc.ui.main.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.ubc.R
import com.example.ubc.data.entities.Control
import com.example.ubc.databinding.DialogControlBinding
import com.example.ubc.ui.main.viewmodels.ControlPanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ControlDialog (
    private var control: Control? = null
) : DialogFragment() {

    private lateinit var _binding: DialogControlBinding
    private val _viewModel: ControlPanelViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogControlBinding.inflate(requireActivity().layoutInflater)

        val builder = AlertDialog.Builder(activity)
            .setView(_binding.root)
            .setPositiveButton(R.string.submit) { _, _ -> applyDialogData() }
            .setNeutralButton(R.string.cancel, null)

        if (control == null) {
            addCreateOptions(builder)
        } else {
            addEditOptions(builder)
        }

        return builder.create()
    }

    private fun addCreateOptions(builder: AlertDialog.Builder) {
        builder.setMessage(R.string.dialog_message_create_control)
    }

    private fun addEditOptions(builder: AlertDialog.Builder) {
        _binding.controlNameEdit.setText(control?.name)
        _binding.controlDataEdit.setText(control?.data)

        builder
            .setMessage(R.string.dialog_message_edit_control)
            .setNegativeButton(R.string.delete) { _, _ ->
                _viewModel.deleteControl(control!!)
            }
    }

    private fun applyDialogData() {
        var controlId = 0
        if (control != null) {
            controlId = control!!.id
        }
        val name = _binding.controlNameEdit.text.toString()
        val data = _binding.controlDataEdit.text.toString()

        _viewModel.saveControl(Control(name, data, controlId))
    }
}