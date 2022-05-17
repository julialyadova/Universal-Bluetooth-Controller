package com.example.ubc.ui.editor

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
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
import com.example.ubc.items.ParamType
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
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _sharedViewModel.panelId.value?.let { _viewModel.init(it) }
    }

    private fun showAddItemDialog() {
        val dialogBinding = DialogCreateItemBinding.inflate(requireActivity().layoutInflater)

        val dialog = AlertDialog.Builder(activity)
            .setView(dialogBinding.root)
            .setTitle(R.string.dialog_add_item_title)
            .setNeutralButton(R.string.cancel, null)
            .create()

        for (itemIdentifier in _viewModel.itemDefinitions) {
            val button = Button(context)
            button.text = itemIdentifier.name
            button.setOnClickListener {
                _viewModel.createItem(itemIdentifier.itemType)
                dialog.cancel()
            }
            button.setOnLongClickListener {
                AlertDialog.Builder(context)
                    .setTitle(itemIdentifier.name)
                    .setMessage(itemIdentifier.description)
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
        binding.itemFormLabel.setText(item.label)

        val onDialogSubmit = mutableListOf<()->Unit>()
        for (param in item.getParams()) {
            val label = TextView(context)
            label.text = param.name

            val form: View = when (param.type) {
                (ParamType.TEXT) -> EditText(context).apply {
                    setText(param.value)
                    onDialogSubmit.add {param.set(getText().toString())}
                }
                (ParamType.INTEGER) -> EditText(context).apply {
                    setText(param.value)
                    inputType = EditorInfo.TYPE_CLASS_NUMBER
                    onDialogSubmit.add {param.set(getText().toString())}
                }
                (ParamType.DECIMAL) -> EditText(context).apply {
                    setText(param.value)
                    inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
                    onDialogSubmit.add {param.set(getText().toString())}
                }
                (ParamType.CATEGORY) -> RadioGroup(context).apply {
                    for (category in param.valuesList) {
                        val radio = RadioButton(context)
                        radio.text = category
                        radio.isChecked = category == param.value
                        radio.setOnCheckedChangeListener { _, isChecked ->
                            onDialogSubmit.add {param.set(category)}
                        }
                        addView(radio)
                    }
                }
                (ParamType.FLAG) -> CheckBox(context).apply {
                    isChecked = param.value.toBoolean()
                    setOnCheckedChangeListener { _, isChecked ->
                        onDialogSubmit.add {param.set(isChecked.toString())}
                    }
                }
                else -> View(context)
            }

            val row = TableRow(context).apply {
                addView(label)
                addView(form)
            }
            binding.itemEditTable.addView(row)
        }

        AlertDialog.Builder(activity)
            .setView(binding.root)
            .setTitle(R.string.dialog_item_edit_title)
            .setMessage(Klaxon().toJsonString(item.getParamValues())) // todo: remove
            .setPositiveButton(R.string.submit) { _, _ ->
                item.label = binding.itemFormLabel.text.toString()
                for (action in onDialogSubmit) {
                    action.invoke()
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