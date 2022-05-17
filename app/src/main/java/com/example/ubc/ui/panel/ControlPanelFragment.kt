package com.example.ubc.ui.panel

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.connection.ConnectionState
import com.example.ubc.databinding.FragmentControlPanelBinding
import com.example.ubc.items.Item
import com.example.ubc.ui.panel.items.ItemViewFactory
import com.example.ubc.ui.shared.PanelSharedViewModel
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
        _binding.btnPanelMenu.setOnClickListener {
            findNavController().popBackStack()
        }
        _binding.btnPanelOptions.setOnClickListener {
            showOptions()
        }

        _viewModel.panel.observe(viewLifecycleOwner) { panel ->
            _binding.textPanelTitle.text = panel.name
        }
        _viewModel.items.observe(viewLifecycleOwner) {items ->
            displayItems(items)
        }
        _viewModel.device.observe(viewLifecycleOwner) {device ->
            if (device != null)
                _binding.textPanelDevice.text = device
        }
        _viewModel.deviceStatus.observe(viewLifecycleOwner) { status ->
            displayDeviceStatus(status)
        }
        _sharedViewModel.panelId.observe(viewLifecycleOwner) { panelId ->
            _viewModel.load(panelId)
        }

        return _binding.root
    }

    private fun displayDeviceStatus(status: ConnectionState) {
        when (status) {
            ConnectionState.Connecting -> {
                _binding.imgPanelConnectionStatus.setImageResource(R.drawable.ic_status_connecting)
                _binding.textPanelDevice.text = "соединение устанавливается..."
            }
            ConnectionState.Connected -> {
                _binding.imgPanelConnectionStatus.setImageResource(R.drawable.ic_status_connected)
            }
            ConnectionState.Disconnecting -> {
                _binding.imgPanelConnectionStatus.setImageResource(R.drawable.ic_status_disconnecting)
            }
            ConnectionState.Disconnected -> {
                _binding.imgPanelConnectionStatus.setImageResource(R.drawable.ic_status_disconnected)
                _binding.textPanelDevice.text = "соединение не установлено"
            }
        }
    }

    private fun displayItems(items: List<Item>) {
        val factory = ItemViewFactory(requireContext())
        _binding.panelCanvas.removeAllViews()
        val itemViews = items.mapNotNull { i -> factory.create(i) }

        for (item in itemViews) {
            item.setOnDataSendActionListener { data ->
                _viewModel.send(data)
            }
            _binding.panelCanvas.addView(item)
        }

        _viewModel.received.observe(viewLifecycleOwner) { data ->
            for (item in itemViews) {
                item.onDataReceived(data)
            }
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