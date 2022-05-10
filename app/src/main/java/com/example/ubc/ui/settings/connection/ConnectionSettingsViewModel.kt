package com.example.ubc.ui.settings.connection

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.connection.*
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
    val adapterIsEnabled = MutableLiveData<Boolean>()
    val scanning = MutableLiveData<Boolean>()
    val devices = MutableLiveData<List<Device>>()
    val activeDevice = MutableLiveData<Device?>()
    val deviceStatus = MutableLiveData<ConnectionState>()

    private var unfilteredDevices : List<Device> = listOf()

    init {
        _connectionService.subscribe(this)
        adapterIsEnabled.value = _connectionService.adapterEnabled()
    }

    override fun onCleared() {
        super.onCleared()
        _connectionService.unsubscribe(this)
    }

    fun scan() {
        GlobalScope.launch {
            _connectionService.scanForAvailableDevices()
        }
    }

    fun cancelScanning() {
        _connectionService.cancelScanning()
    }

    fun disconnect() {
        _connectionService.disconnect()
    }

    fun onDeviceClicked(device: Device) {
        connect(Device(device.name, device.address))
    }

    fun send(data: String) {
        _connectionService.send(data)
    }

    fun enableRequiredOption() {
        _connectionService.enableRequiredOption()
    }

    fun disableRequiredOption() {
        _connectionService.disableRequiredOption()
    }

    private fun connect(device: Device) {
        _connectionService.connect(device)
    }

    private fun setActiveDevice(device: Device) {
        activeDevice.value = device
        devices.value = unfilteredDevices.filter { d -> d != device }
    }

    private fun removeActiveDevice() {
        activeDevice.value = null
        devices.value = unfilteredDevices
    }

    override fun onConnectionStatusChanged(state: ConnectionState, device: Device?) {
        Log.d("ConnectionViewModel", "onConnectionStatusChanged: $state ${device?.address}")
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                when (state) {
                    ConnectionState.Disconnected -> {
                        removeActiveDevice()
                    }
                    ConnectionState.Connecting -> {
                        setActiveDevice(device ?: Device("Устройство без имени", ""))
                    }
                }
                deviceStatus.value = state
            }
        }
    }

    override fun onAdapterStateChanged(state: AdapterState) {
        when (state){
            AdapterState.Enabled -> adapterIsEnabled.value = true
            AdapterState.Disabled -> adapterIsEnabled.value = false
            AdapterState.StartedScanning -> scanning.value = true
            AdapterState.FinishedScanning -> scanning.value = false

        }

    }

    override fun onDataReceived(data: ByteArray) {}

    override fun onAvailableDevicesFound(foundDevices: Collection<Device>) {
        unfilteredDevices = foundDevices.toList()
        devices.value = unfilteredDevices
    }
}