package com.example.ubc.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.ubc.R
import com.example.ubc.connection.ConnectionState
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide
            .with(requireContext())
            .load(R.drawable.loading)
            .into(_binding.imgConnectionScanning)
        _binding.imgConnectionScanning.visibility = View.INVISIBLE

        _binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        _binding.connectionActiveDeviceCard.setOnClickListener {
            _viewModel.disconnect()
        }
        _binding.switchConnectionRequiredOption.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                _viewModel.enableRequiredOption()
            }
            else
                _viewModel.disableRequiredOption()
        }
        _binding.textConnectionProfileName.setOnClickListener {
            _viewModel.scan()
        }
        _binding.btnConnectionScan.setOnClickListener {
            _viewModel.scan()
        }
        _binding.btnConnectionCancelScanning.setOnClickListener {
            _viewModel.cancelScanning()
        }

        _viewModel.activeDevice.observe(viewLifecycleOwner) { device ->
            displayActiveDevice(device)
        }
        _viewModel.deviceStatus.observe(viewLifecycleOwner) { status ->
            displayStatus(status)
        }
        _viewModel.adapterIsEnabled.observe(viewLifecycleOwner) { enabled ->
            displayAdapterState(enabled)
        }
        _viewModel.devices.observe(viewLifecycleOwner) { devices ->
            showDevices(devices)
        }
        _viewModel.scanning.observe(viewLifecycleOwner) { scanning ->
            displayScanningState(scanning)
        }
    }

    private fun displayAdapterState(enabled: Boolean) {
        _binding.btnConnectionScan.isEnabled = enabled
        _binding.btnConnectionScan.alpha = if (enabled) 1f else 0.4f
        _binding.switchConnectionRequiredOption.isChecked = enabled
    }

    private fun displayScanningState(scanning: Boolean) {
        if (scanning) {
            _binding.imgConnectionScanning.visibility = View.VISIBLE
            _binding.btnConnectionCancelScanning.visibility = View.VISIBLE
            _binding.btnConnectionScan.visibility = View.INVISIBLE
        } else {
            _binding.imgConnectionScanning.visibility = View.INVISIBLE
            _binding.btnConnectionCancelScanning.visibility = View.INVISIBLE
            _binding.btnConnectionScan.visibility = View.VISIBLE
        }
    }

    private fun displayStatus(state: ConnectionState) {
        when (state) {
            ConnectionState.Connecting -> {
                Glide
                    .with(requireContext())
                    .load(R.drawable.loading)
                    .into(_binding.connectionActiveDeviceStatusImg)
            }
            ConnectionState.Connected -> {
                setActiveDeviceStatusImg(R.drawable.ic_status_connected)
            }
            ConnectionState.Disconnecting -> {
                setActiveDeviceStatusImg(R.drawable.ic_status_disconnecting)
            }
            ConnectionState.Disconnected -> {
                setActiveDeviceStatusImg(R.drawable.ic_status_disconnected)
                makeToast(R.string.connection_status_disconnected)
            }
        }
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
            itemBinding.itemDeviceInfo.text = device.info

            if (_viewModel.activeDevice.value == null) {
                itemBinding.root.setOnClickListener {
                    _viewModel.onDeviceClicked(device)
                }
            } else {
                itemBinding.root.alpha = 0.5f
            }


            _binding.pairedDevices.addView(itemBinding.root)
        }
    }

    private fun setActiveDeviceStatusImg(@DrawableRes res: Int) {
        _binding.connectionActiveDeviceStatusImg.setImageResource(res)
    }

    private fun makeToast(@StringRes res: Int) {
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show()
    }
}