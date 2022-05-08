package com.example.ubc.ui.main.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ubc.R
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.connection.Device
import com.example.ubc.databinding.FragmentConnectionSettingsBinding
import com.example.ubc.databinding.ItemDeviceBinding
import com.example.ubc.ui.main.viewmodels.ConnectionSettingsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ConnectionSettingsFragment : Fragment() {

    private lateinit var _binding : FragmentConnectionSettingsBinding
    private val _viewModel: ConnectionSettingsViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionSettingsBinding.inflate(layoutInflater)
        return _binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ConnectionSettingsFragment", "onDestroy")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        _binding.switchConnectionRequiredOption.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                _viewModel.enableRequiredOption()
            else
                _viewModel.disableRequiredOption()
        }

        _viewModel.activeDevice.observe(viewLifecycleOwner) { device ->
            displayActiveDevice(device)
        }
        _viewModel.deviceStatus.observe(viewLifecycleOwner) { status ->
            displayStatus(status)
        }
        _viewModel.requiredOptionEnabled.observe(viewLifecycleOwner) { enabled ->
            _binding.textConnectionProfileName.text = if (enabled) "вкл" else "выкл"
        }
        _viewModel.devices.observe(viewLifecycleOwner) { devices ->
            showDevices(devices)
        }
        _viewModel.updateDevices()
    }

    private fun displayStatus(status: ConnectionStatus) {
        if (status == ConnectionStatus.Connecting)
            _binding.connectionActiveDeviceStatusImg.setImageResource(android.R.drawable.presence_away)
        else if (status == ConnectionStatus.Connected)
            _binding.connectionActiveDeviceStatusImg.setImageResource(android.R.drawable.presence_online)
        else if (status == ConnectionStatus.Disconnecting)
            _binding.connectionActiveDeviceStatusImg.setImageResource(android.R.drawable.presence_busy)
        else if (status == ConnectionStatus.Disconnected)
            _binding.connectionActiveDeviceStatusImg.setImageResource(android.R.drawable.presence_invisible)

        Toast.makeText(context, status.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun displayActiveDevice(device: Device?) {
        if (device == null) {
            _binding.connectionActiveDeviceCard.visibility = View.INVISIBLE
        }
        else {
            _binding.connectionActiveDeviceCard.visibility = View.VISIBLE
            _binding.connectionActiveDeviceName.text = device.name ?: "Устройство без имени"
            _binding.connectionActiveDeviceMac.text = device.address
        }
    }

    private fun showDevices(devices: List<Device>) {
        val inflater = LayoutInflater.from(context)
        _binding.pairedDevices.removeAllViews()

        for (device in devices) {
            val itemBinding = ItemDeviceBinding.inflate(inflater)
            itemBinding.itemDeviceName.text = device.name ?: "Устройство без имени"
            itemBinding.itemDeviceMac.text = device.address

            itemBinding.root.setOnClickListener() {
                _viewModel.onDeviceClicked(device)
            }

            _binding.pairedDevices.addView(itemBinding.root)
        }
    }

    override fun onResume() {
        super.onResume()
        _viewModel.update()
    }
}