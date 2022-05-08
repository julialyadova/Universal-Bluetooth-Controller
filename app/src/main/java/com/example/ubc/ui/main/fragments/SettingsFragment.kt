package com.example.ubc.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.connection.Device
import com.example.ubc.databinding.FragmentSettingsBinding
import com.example.ubc.databinding.ItemDeviceBinding
import com.example.ubc.ui.main.viewmodels.ConnectionViewModel
import com.example.ubc.ui.main.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var _binding : FragmentSettingsBinding
    private val _connectionViewModel: ConnectionViewModel by activityViewModels()
    private val _settingsViewModel: SettingsViewModel by activityViewModels()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_controlPanelFragment)
        }

        _binding.bluetoothSwitch.setOnClickListener() {
            _settingsViewModel.switchBluetooth()
        }

        _settingsViewModel.bluetoothEnabled.observe(viewLifecycleOwner) {bluetoothEnabled ->
            _binding.bluetoothSwitch.isChecked = bluetoothEnabled
        }

        _connectionViewModel.availableDevices.observe(viewLifecycleOwner){ devices ->
            showDevices(LayoutInflater.from(context), devices)
        }
        
        _connectionViewModel.findDevices()
    }

    private fun showDevices(inflater: LayoutInflater, devices: List<Device>) {
        _binding.pairedDevices.removeAllViews()

        for (device in devices) {
            val itemBinding = ItemDeviceBinding.inflate(inflater)
            itemBinding.itemDeviceName.text = device.name
            itemBinding.itemDeviceMac.text = device.address

            if (device.address == _connectionViewModel.activeDevice.value?.address) {
                itemBinding.itemDeviceActive.visibility = View.VISIBLE
            }

            itemBinding.root.setOnClickListener() {
                _connectionViewModel.onDeviceClicked(device)
                _connectionViewModel.findDevices()
            }

            _binding.pairedDevices.addView(itemBinding.root)
        }
    }
}