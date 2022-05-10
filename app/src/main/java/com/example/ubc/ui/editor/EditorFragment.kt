package com.example.ubc.ui.editor

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
import com.example.ubc.databinding.DialogEditItemBinding
import com.example.ubc.databinding.DialogPanelBinding
import com.example.ubc.databinding.FragmentEditorBinding
import com.example.ubc.ui.shared.PanelSharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditorFragment : Fragment() {

    private lateinit var _binding : FragmentEditorBinding
    private val _viewModel: EditorViewModel by viewModels()
    private val _sharedViewModel: PanelSharedViewModel by activityViewModels()
    private lateinit var _editor : EditorCanvas

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(layoutInflater)
        _binding.btnEditorOptions.setOnClickListener { showOptionsMenu(it) }
        _binding.btnEditorAddItem.setOnClickListener { showAddItemDialog() }
        _binding.btnEditorRenamePanel.setOnClickListener { showRenamePanelDialog() }

        _editor = EditorCanvas(_binding.canvas)
        _editor.setOnItemMovedListener { itemId, x, y ->
            _viewModel.setPosition(itemId, x, y)
        }
        _editor.setOnItemClickListener { item ->
            showEditItemDialog(item)
        }

        _viewModel.items.observe(viewLifecycleOwner) { items ->
            _editor.setItems(items)
        }

        _viewModel.panel.observe(viewLifecycleOwner) {panel ->
            _binding.title.text = panel.name
        }
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _sharedViewModel.panelId.value?.let { _viewModel.init(it) }
    }

    private fun showAddItemDialog() {
        val binding = DialogCreateItemBinding.inflate(requireActivity().layoutInflater)

        val dialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setTitle(R.string.dialog_add_item_title)
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
        binding.btnCreateItemHistory.setOnClickListener {
            _viewModel.createItem(Item.Types.HISTORY)
            dialog.cancel()
        }

        dialog.show()
    }

    private fun showEditItemDialog(item: Item) {
        val binding = DialogEditItemBinding.inflate(requireActivity().layoutInflater)
        binding.itemFormLabel.setText(item.label)
        binding.itemFormData.setText(item.data)
        binding.itemFormFormatAscii.isChecked = item.format == Item.DataFormats.ASCII
        binding.itemFormFormatHex.isChecked = item.format == Item.DataFormats.HEX


        AlertDialog.Builder(activity)
            .setView(binding.root)
            .setTitle(R.string.dialog_item_edit_title)
            .setPositiveButton(R.string.submit) { _, _ ->
                item.label = binding.itemFormLabel.text.toString()
                item.data = binding.itemFormData.text.toString()
                _viewModel.update(item)
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
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
                findNavController().popBackStack()
                true
            }
            R.id.editor_options_add_item -> {
                showAddItemDialog()
                true
            }
            R.id.editor_options_help -> {
                showHelp()
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
            .setTitle(R.string.dialog_rename_panel_title)
            .setPositiveButton(R.string.dialog_rename_panel_action_rename) { _, _ ->
                _viewModel.renamePanel(binding.createPanelName.text.toString())
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
            .show()
    }

    private fun showHelp() {
        AlertDialog.Builder(activity)
            .setMessage(R.string.dialog_editor_help_text)
            .setTitle(R.string.dialog_editor_help_title)
            .setPositiveButton(R.string.submit, null)
            .create()
            .show()
    }
}