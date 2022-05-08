package com.example.ubc.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.connection.ConnectionListener
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.connection.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConnectionSettingsViewModel @Inject constructor(
        private val _connectionService: ConnectionService
) : ViewModel(), ConnectionListener {
    val requiredOptionEnabled = MutableLiveData<Boolean>()
    val devices = MutableLiveData<List<Device>>()
    val activeDevice = MutableLiveData<Device?>()
    val deviceStatus = MutableLiveData<ConnectionStatus>()

    init {
        _connectionService.subscribe(this)
    }

    override fun onCleared() {
        super.onCleared()
        _connectionService.unsubscribe(this)
    }

    fun update() {

    }

    fun updateDevices() {
        devices.value = _connectionService.getAvailableDevices()
            .filter { d -> d.address != activeDevice.value?.address }
    }

    fun onDeviceClicked(device: Device) {
        connect(Device(device.name, device.address))
        updateDevices()
    }

    fun send(data: String) {
        _connectionService.send(data)
    }

    fun enableRequiredOption() {
        _connectionService.enableRequiredOption()
        updateDevices()
    }

    fun disableRequiredOption() {
        _connectionService.disableRequiredOption()
    }

    private fun connect(device: Device) {
        _connectionService.connect(device)
    }

    override fun onConnectionStatusChanged(status: ConnectionStatus, device: Device?) {
        Log.d("ConnectionViewModel", "onConnectionStatusChanged: $status ${device?.address}")
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                when (status) {
                    ConnectionStatus.RequiredOptionDisabled -> {
                        requiredOptionEnabled.value = false
                    }
                    ConnectionStatus.RequiredOptionEnabled -> {
                        requiredOptionEnabled.value = true
                    }
                    ConnectionStatus.Disconnected -> {
                        activeDevice.value = null
                    }
                    else -> {
                        activeDevice.value = device
                        deviceStatus.value = status
                    }
                }
                updateDevices()
            }
        }
    }

    override fun onDataReceived(data: ByteArray) {}
}