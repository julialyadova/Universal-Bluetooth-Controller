package com.example.ubc.ui.menu

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ubc.R
import com.example.ubc.databinding.DialogCreatePanelBinding
import com.example.ubc.databinding.FragmentMenuBinding
import com.example.ubc.items.Panel
import com.example.ubc.ui.shared.AppDialogBuilder
import com.example.ubc.ui.shared.PanelSharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private lateinit var _binding : FragmentMenuBinding
    private val _viewModel: MenuViewModel by viewModels()
    private val _sharedViewModel: PanelSharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.btnMenuCreatePanel.setOnClickListener {
            showCreatePanelDialog()
        }
        _binding.btnMenuOptionsMenu.setOnClickListener { showMenu(it) }

        _viewModel.newlyCreatedPanelId.observe(viewLifecycleOwner) { id ->
            _sharedViewModel.selectPanel(id)
            findNavController().navigate(R.id.action_menuFragment_to_controlPanelFragment)
        }

        initItemsList()
        _viewModel.loadMenu()
    }

    private fun initItemsList() {
        val adapter = PanelItemAdapter(this::onPanelSelected,this::showDeletePanelDialog)

        val recyclerView: RecyclerView = _binding.panelsRecyclerView
        recyclerView.adapter = adapter

        _viewModel.panelsList.observe(viewLifecycleOwner,{ panels ->
            adapter.setItems(panels)
        })
    }

    private fun onPanelSelected(panel: Panel) {
        _sharedViewModel.selectPanel(panel.id)
        findNavController().navigate(R.id.action_menuFragment_to_controlPanelFragment)
    }

    private fun showDeletePanelDialog(panel: Panel) {
        AppDialogBuilder(activity)
            .setTitle("${getString(R.string.dialog_delete_panel_title)} \"${panel.name}\"?")
            .setPositiveButton(R.string.delete) { _, _ ->
                _viewModel.deletePanel(panel)
            }
            .setNeutralButton(R.string.cancel, null)
            .create().show()
    }

    private fun showCreatePanelDialog() {
        val binding = DialogCreatePanelBinding.inflate(requireActivity().layoutInflater)
        AppDialogBuilder(activity)
            .setView(binding.root)
            .setTitle(R.string.dialog_create_panel_title)
            .setPositiveButton(R.string.create) { _, _ ->
                val name = binding.dialogCreatePanelInput.text.toString()
                val horizontal = binding.dialogCreatePanelOrientationHorizontal.isChecked
                _viewModel.createPanel(name, horizontal)
            }
            .setNegativeButton(R.string.cancel, null)
            .create().show()
    }

    private fun showMenu(v: View) {
        val popup = PopupMenu(context, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_options_menu, popup.menu)
        popup.setOnMenuItemClickListener{item -> onMenuItemClick(item)}
        popup.show()
    }

    private fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_options_settings -> {
                findNavController().navigate(R.id.action_menuFragment_to_connectionSettingsFragment)
                true
            }
            else -> false
        }
    }
}