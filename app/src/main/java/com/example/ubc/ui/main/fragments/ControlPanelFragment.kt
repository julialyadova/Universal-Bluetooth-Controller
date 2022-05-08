package com.example.ubc.ui.main.fragments

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.FragmentControlPanelBinding
import com.example.ubc.ui.items.ItemViewFactory
import com.example.ubc.ui.main.viewmodels.ControlPanelViewModel
import com.example.ubc.ui.main.viewmodels.PanelSharedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ControlPanelFragment : Fragment() {

    private lateinit var _binding : FragmentControlPanelBinding
    private val _viewModel: ControlPanelViewModel by viewModels()
    private val  _sharedViewModel: PanelSharedViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlPanelBinding.inflate(layoutInflater)
        return _binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.btnPanelMenu.setOnClickListener { navigateToMenu() }
        _binding.btnPanelOptions.setOnClickListener { showOptions() }

        _viewModel.panel.observe(viewLifecycleOwner) { panel ->
            _binding.textPanelTitle.text = panel.name
        }
        _viewModel.items.observe(viewLifecycleOwner) {items ->
            displayItems(items, viewLifecycleOwner)
        }
        _viewModel.device.observe(viewLifecycleOwner) {device ->
            _binding.textPanelDevice.text = device
        }
        _viewModel.deviceStatus.observe(viewLifecycleOwner) { status ->
            if (status == ConnectionStatus.Connecting)
                _binding.imgPanelConnectionStatus.setImageResource(android.R.drawable.presence_away)
            else if (status == ConnectionStatus.Connected)
                _binding.imgPanelConnectionStatus.setImageResource(android.R.drawable.presence_online)
            else if (status == ConnectionStatus.Disconnecting)
                _binding.imgPanelConnectionStatus.setImageResource(android.R.drawable.presence_busy)
            else if (status == ConnectionStatus.Disconnected)
                _binding.imgPanelConnectionStatus.setImageResource(android.R.drawable.presence_invisible)
        }
        _sharedViewModel.panelId.observe(viewLifecycleOwner) { panelId ->
            _viewModel.load(panelId)
        }
    }

    private fun navigateToMenu() {
        findNavController().popBackStack()
    }

    private fun displayItems(items: List<Item>, lifecycleOwner: LifecycleOwner) {
        val factory = ItemViewFactory(requireContext(), _viewModel, lifecycleOwner)
        _binding.panelCanvas.removeAllViews()
        for (item in items) {
            val itemView = factory.create(item) ?: continue
            _binding.panelCanvas.addView(itemView)
        }
    }

    private fun showOptions() {
        val popup = PopupMenu(context, _binding.btnPanelOptions)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.controlpanel_options_menu, popup.menu)
        popup.setOnMenuItemClickListener{item -> onMenuOptionClick(item)}
        popup.show()
    }


    private fun onMenuOptionClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.panel_options_edit -> {
                findNavController().navigate(R.id.action_controlPanelFragment_to_editorFragment)
                true
            }
            R.id.panel_options_connection_settings -> {
                findNavController().navigate(R.id.action_controlPanelFragment_to_connectionSettingsFragment)
                true
            }
            R.id.panel_options_panel_settings -> {
                // to panel settings
                true
            }
            else -> false
        }
    }
}