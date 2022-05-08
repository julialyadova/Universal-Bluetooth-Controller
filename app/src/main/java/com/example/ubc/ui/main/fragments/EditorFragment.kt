package com.example.ubc.ui.main.fragments

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.DialogCreateItemBinding
import com.example.ubc.databinding.DialogPanelBinding
import com.example.ubc.databinding.FragmentEditorBinding
import com.example.ubc.ui.editor.EditableCanvas
import com.example.ubc.ui.main.viewmodels.EditorViewModel
import com.example.ubc.ui.main.viewmodels.PanelSharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditorFragment : Fragment() {

    private lateinit var _binding : FragmentEditorBinding
    val _viewModel: EditorViewModel by viewModels()
    private val _sharedViewModel: PanelSharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(layoutInflater)
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.btnEditorOptions.setOnClickListener { showOptionsMenu(it) }
        _binding.btnEditorAddItem.setOnClickListener { showAddItemDialog() }
        _binding.btnEditorRenamePanel.setOnClickListener { showRenamePanelDialog() }

        var editor = EditableCanvas(_binding.canvas, this)
        _viewModel.items.observe(viewLifecycleOwner) { items ->
            editor.update(items)
        }

        _viewModel.panel.observe(viewLifecycleOwner) {panel ->
            _binding.title.text = panel.name
        }

        _sharedViewModel.panelId.value?.let { _viewModel.init(it) }
    }

    private fun showAddItemDialog() {
        val binding = DialogCreateItemBinding.inflate(requireActivity().layoutInflater)

        val dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setMessage(R.string.dialog_message_create_control)
            .setNeutralButton(R.string.cancel, null)
            .create()

        binding.btnCreateItemButton.setOnClickListener {
            _viewModel.createItem(Item.Types.BUTTON)
            dialog.cancel()
        }
        binding.btnCreateItemSwitch.setOnClickListener {
            _viewModel.createItem(Item.Types.SWITCH)
            dialog.cancel()
        }

        dialog.show()
    }

    private fun showOptionsMenu(v: View) {
        val popup = PopupMenu(context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.editor_options_menu, popup.menu)
        popup.setOnMenuItemClickListener{item -> onMenuItemClick(item)}
        popup.show()
    }


    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editor_options_quit -> {
                findNavController().navigate(R.id.action_editorFragment_to_controlPanelFragment)
                true
            }
            R.id.editor_options_add_item -> {
                showAddItemDialog()
                true
            }
            else -> false
        }
    }

    private fun showRenamePanelDialog() {
        val binding = DialogPanelBinding.inflate(requireActivity().layoutInflater)
        binding.createPanelName.setText(_viewModel.panel.value?.name)

        AlertDialog.Builder(activity)
            .setView(binding.root)
            .setMessage(R.string.dialog_message_rename_panel)
            .setPositiveButton(R.string.rename) { dialog, id ->
                _viewModel.renamePanel(binding.createPanelName.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }
}