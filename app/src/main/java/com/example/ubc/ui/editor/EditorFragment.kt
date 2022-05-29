package com.example.ubc.ui.editor

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.beust.klaxon.Klaxon
import com.example.ubc.R
import com.example.ubc.databinding.DialogCreateItemBinding
import com.example.ubc.databinding.DialogEditItemBinding
import com.example.ubc.databinding.DialogPanelBinding
import com.example.ubc.databinding.FragmentEditorBinding
import com.example.ubc.items.Item
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
        setTrashBin(_binding.btnEditorAddItem)

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
        _viewModel.notFound.observe(viewLifecycleOwner) {notFound ->
            if (notFound)
                findNavController().popBackStack()
        }
        _sharedViewModel.panelId.observe(viewLifecycleOwner) {panelId ->
            _viewModel.init(panelId)
        }
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun showAddItemDialog() {
        val dialogBinding = DialogCreateItemBinding.inflate(requireActivity().layoutInflater)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
            .setTitle(R.string.dialog_add_item_title)
            .setNeutralButton(R.string.cancel, null)
            .create()

        for (itemDefinition in _viewModel.itemDefinitions) {
            val button = Button(context)
            button.text = itemDefinition.value.name
            button.setOnClickListener {
                _viewModel.createItem(itemDefinition.key)
                dialog.cancel()
            }
            button.setOnLongClickListener {
                AlertDialog.Builder(context)
                    .setTitle(itemDefinition.value.name)
                    .setMessage(itemDefinition.value.description)
                    .setPositiveButton(R.string.submit, null)
                    .create()
                    .show()
                true
            }
            dialogBinding.createItemButtons.addView(button)
        }

        dialog.show()
    }

    private fun showEditItemDialog(item: Item) {
        val binding = DialogEditItemBinding.inflate(requireActivity().layoutInflater)
        binding.itemEditForm.setText(item.label)

        val params = item.getEditDialogParams()
        for (param in params) {
            val label = TextView(context)
            label.text = param.name

            binding.root.addView(label)
            binding.root.addView(param.createView(binding.root.context))
        }

        AlertDialog.Builder(activity)
            .setView(binding.root)
            .setTitle(R.string.dialog_item_edit_title)
            .setMessage(Klaxon().toJsonString(item.getParamValues())) // todo: remove
            .setPositiveButton(R.string.submit) { _, _ ->
                item.label = binding.itemEditForm.text.toString()
                for (param in params) {
                    param.submit()
                }
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

    private fun setTrashBin(view: View) {
        val resource = view.background
        view.setOnDragListener { v, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.setBackgroundResource(R.drawable.ic_recycle_bin)
                }
                DragEvent.ACTION_DROP -> {
                    val itemId = Integer.parseInt(e.clipData.getItemAt(0).text.toString())
                    _viewModel.delete(itemId)
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.background = resource
                }
            }
            true
        }
    }
}