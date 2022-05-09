package com.example.ubc.ui.main.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.ubc.R
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.DialogControlBinding
import com.example.ubc.ui.main.viewmodels.EditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDialog (
    private var item: Item? = null
) : DialogFragment() {

    private lateinit var _binding: DialogControlBinding
    private val _editorViewModel: EditorViewModel by activityViewModels()

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
        builder.setMessage(R.string.dialog_add_item_title)
    }

    private fun addEditOptions(builder: AlertDialog.Builder) {
        _binding.controlNameEdit.setText(item?.label)
        _binding.controlDataEdit.setText(item?.data)

        builder
            .setMessage(R.string.dialog_message_edit_control)
            .setNegativeButton(R.string.delete) { _, _ ->
                _editorViewModel.delete(item!!)
            }
    }

    private fun applyDialogData() {
        val id = item?.id ?: 0
        val label = _binding.controlNameEdit.text.toString()
        val data = _binding.controlDataEdit.text.toString()
        val panelId = _editorViewModel.panel.value!!.id
        val type = when (data) {
            "b" -> Item.Types.BUTTON
            "s" -> Item.Types.SWITCH
            "d" -> Item.Types.SIMPLE_DISPLAY
            "h" -> Item.Types.HISTORY
            else -> Item.Types.BUTTON
        }
        _editorViewModel.save(Item(id, panelId, label, type, data, Item.DataFormats.ASCII))
    }
}