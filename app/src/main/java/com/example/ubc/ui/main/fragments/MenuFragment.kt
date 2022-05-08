package com.example.ubc.ui.main.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tacos.adapters.PanelItemAdapter
import com.example.ubc.R
import com.example.ubc.data.entities.Panel
import com.example.ubc.databinding.DialogPanelBinding
import com.example.ubc.databinding.FragmentMenuBinding
import com.example.ubc.ui.main.viewmodels.MenuViewModel
import com.example.ubc.ui.main.viewmodels.PanelSharedViewModel
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
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.btnMenuCreatePanel.setOnClickListener {
            showCreatePanelDialog()
        }

        _binding.btnMenuoptionsMenu.setOnClickListener { showMenu(it) }

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
        AlertDialog.Builder(activity)
            .setMessage("Delete panel \"${panel.name}\"?")
            .setPositiveButton("delete") { _, _ ->
                _viewModel.deletePanel(panel)
            }
            .setNeutralButton(R.string.cancel, null)
            .create().show()
    }

    private fun showCreatePanelDialog() {
        val binding = DialogPanelBinding.inflate(requireActivity().layoutInflater)
        AlertDialog.Builder(activity)
            .setView(binding.root)
            .setMessage(R.string.dialog_message_create_panel)
            .setPositiveButton(R.string.submit) { _, _ ->
                _viewModel.createPanel(binding.createPanelName.text.toString())
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