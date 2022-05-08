package com.example.ubc.ui.main.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.ubc.R
import com.example.ubc.databinding.DialogPanelBinding
import com.example.ubc.ui.main.viewmodels.ControlPanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RenamePanelDialog : DialogFragment() {

    private val _viewModel: ControlPanelViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogPanelBinding.inflate(requireActivity().layoutInflater)

        return AlertDialog.Builder(activity)
            .setView(binding.root)
            .setMessage(R.string.dialog_message_rename_panel)
            .setPositiveButton(R.string.submit) { dialog, id ->
                //_viewModel.rename(binding.createPanelName.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}