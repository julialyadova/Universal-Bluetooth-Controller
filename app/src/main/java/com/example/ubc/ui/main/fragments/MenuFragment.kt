package com.example.ubc.ui.main.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.tacos.adapters.PanelItemAdapter
import com.example.ubc.R
import com.example.ubc.data.entities.Panel
import com.example.ubc.databinding.FragmentMenuBinding
import com.example.ubc.ui.main.dialogs.CreatePanelDialog
import com.example.ubc.ui.main.viewmodels.MenuViewModel
import com.example.ubc.ui.main.viewmodels.PanelViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private lateinit var _binding : FragmentMenuBinding
    private val _viewModel: MenuViewModel by viewModels()
    private val _panelViewModel: PanelViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.button.setOnClickListener {
            CreatePanelDialog().show(parentFragmentManager, "dialog")
        }

        _binding.optionsButtonMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_settingsFragment)
        }

        val adapter = PanelItemAdapter(this::navigateToPanel,this::showDeletePanelDialog)

        val recyclerView: RecyclerView = _binding.panelsRecyclerView
        recyclerView.adapter = adapter

        _viewModel.controlPanels.observe(viewLifecycleOwner,{ panels ->
            adapter.setItems(panels)
        })

        _viewModel.loadPanels()
    }

    private fun navigateToPanel(panel: Panel) {
        _panelViewModel.load(panel.id)
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

}