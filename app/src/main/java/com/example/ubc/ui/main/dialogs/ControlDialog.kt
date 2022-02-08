package com.example.ubc.ui.main.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.ubc.R
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.DialogControlBinding
import com.example.ubc.ui.items.ItemTypes
import com.example.ubc.ui.main.viewmodels.ItemsViewModel
import com.example.ubc.ui.main.viewmodels.PanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ControlDialog (
    private var item: Item? = null
) : DialogFragment() {

    private lateinit var _binding: DialogControlBinding
    private val _itemsViewModel: ItemsViewModel by activityViewModels()
    private val _panelViewModel: PanelViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = DialogControlBinding.inflate(requireActivity().layoutInflater)

        val builder = AlertDialog.Builder(activity)
            .setView(_binding.root)
            .setPositiveButton(R.string.submit) { _, _ -> applyDialogData() }
            .setNeutralButton(R.string.cancel, null)

        if (item == null) {
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
        _binding.controlNameEdit.setText(item?.name)
        _binding.controlDataEdit.setText(item?.data)

        builder
            .setMessage(R.string.dialog_message_edit_control)
            .setNegativeButton(R.string.delete) { _, _ ->
                _itemsViewModel.delete(item!!)
            }
    }

    private fun applyDialogData() {
        var controlId = 0
        if (item != null) {
            controlId = item!!.id
        }
        val name = _binding.controlNameEdit.text.toString()
        val data = _binding.controlDataEdit.text.toString()

        _itemsViewModel.save(Item(name, data, ItemTypes.SWITCH, controlId,_panelViewModel.panel.value!!.id))
    }
}