package com.example.ubc.ui.main.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.databinding.DialogPanelBinding
import com.example.ubc.ui.main.viewmodels.PanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreatePanelDialog : DialogFragment() {

    private val _viewModel: PanelViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogPanelBinding.inflate(requireActivity().layoutInflater)

        return AlertDialog.Builder(activity)
            .setView(binding.root)
            .setMessage(R.string.dialog_message_create_panel)
            .setPositiveButton(R.string.submit) { _, _ ->
                _viewModel.create(binding.createPanelName.text.toString())
                findNavController().navigate(R.id.action_menuFragment_to_controlPanelFragment)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
}