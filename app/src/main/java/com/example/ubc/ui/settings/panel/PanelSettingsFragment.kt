package com.example.ubc.ui.settings.panel

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.databinding.DialogRenamePanelBinding
import com.example.ubc.databinding.FragmentPanelSettingsBinding
import com.example.ubc.ui.shared.AppDialogBuilder
import com.example.ubc.ui.shared.PanelSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PanelSettingsFragment : Fragment() {

    private lateinit var _binding : FragmentPanelSettingsBinding
    private val  _sharedViewModel: PanelSharedViewModel by activityViewModels()
    private val _viewModel: PanelSettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        _binding = FragmentPanelSettingsBinding.inflate(layoutInflater)
        _binding.panelSettingsDeleteButton.setOnClickListener {
            showDeletePanelDialog()
        }
        _binding.panelSettingsName.setOnClickListener {
            showRenamePanelDialog()
        }
        _binding.panelSettingsBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        _sharedViewModel.panelId.observe(viewLifecycleOwner) {panelId ->
            if (panelId != -1)
                _viewModel.load(panelId)
        }
        _viewModel.panel.observe(viewLifecycleOwner) { panel ->
            _binding.panelSettingsName.text = panel.name
        }
        _viewModel.panelDeleted.observe(viewLifecycleOwner) { deleted ->
            if (deleted) {
                findNavController().popBackStack()
            }
        }

        return _binding.root
    }

    private fun showRenamePanelDialog() {
        val binding = DialogRenamePanelBinding.inflate(requireActivity().layoutInflater)
        binding.dialogRenamePanelInput.setText(_viewModel.panel.value?.name)


        AppDialogBuilder(activity)
            .setView(binding.root)
            .setTitle(R.string.dialog_rename_panel_title)
            .setPositiveButton(R.string.dialog_rename_panel_action_rename) { _, _ ->
                _viewModel.renamePanel(binding.dialogRenamePanelInput.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    private fun showDeletePanelDialog() {
        AppDialogBuilder(activity)
            .setTitle("${getString(R.string.dialog_delete_panel_title)} \"${_viewModel.panel.value?.name}\"?")
            .setPositiveButton(R.string.delete) { _, _ ->
                _viewModel.delete()
            }
            .setNeutralButton(R.string.cancel, null)
            .create().show()
    }
}