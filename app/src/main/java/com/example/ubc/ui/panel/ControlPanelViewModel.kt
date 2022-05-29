package com.example.ubc.ui.panel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.connection.*
import com.example.ubc.data.ControlPanelService
import com.example.ubc.items.Item
import com.example.ubc.items.Panel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ControlPanelViewModel @Inject constructor(
        private val _controlPanelService: ControlPanelService,
        private val _connectionService: ConnectionService
) : ViewModel(), ConnectionListener {
    val panel = MutableLiveData<Panel>()
    val items = MutableLiveData<List<Item>>()
    val device = MutableLiveData<String?>()
    val deviceStatus = MutableLiveData<ConnectionState>()
    val received = MutableLiveData<ByteArray>()
    val isLoading = MutableLiveData<Boolean>()

    init {
        deviceStatus.value = _connectionService.getConnectionStatus()
        device.value = _connectionService.getConnectedDevice()?.name
        _connectionService.subscribe(this)
    }

    override fun onCleared() {
        _connectionService.unsubscribe(this)
    }

    fun load(id: Int) {
        isLoading.value = true
        GlobalScope.launch(Dispatchers.IO) {
            val panelData = _controlPanelService.getPanelById(id)
            val itemsList = _controlPanelService.getItemsOfPanelWithId(id)
            val connectedDevice = _connectionService.getConnectedDevice()
            val connectionStatus = _connectionService.getConnectionStatus()
            withContext(Dispatchers.Main) {
                panel.value = panelData
                items.value = itemsList
                deviceStatus.value = connectionStatus
                device.value = connectedDevice?.name

                deviceStatus.value = _connectionService.getConnectionStatus()
                device.value = _connectionService.getConnectedDevice()?.name

                isLoading.value = false
            }
        }
    }

    fun send(data: ByteArray) {
        Log.d("Control Panel ViewModel", "send: ${String(data)} (${data.size})")
        _connectionService.send(data)
    }

    override fun onConnectionStatusChanged(state: ConnectionState, targetDevice: Device?) {
        Log.d("ControlPanelViewModel", "onConnectionStatusChanged: $state ${targetDevice?.address}")
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                if (targetDevice != null) {
                    device.value = targetDevice.name ?: "Устройстов без имени"
                    deviceStatus.value = state
                }
            }
        }
    }

    override fun onAdapterStateChanged(state: AdapterState) {
        if (state == AdapterState.Disabled) {
            deviceStatus.value = ConnectionState.Disconnected
        }
    }

    override fun onDataReceived(data: ByteArray) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                received.value = data
            }
        }
    }

    override fun onAvailableDevicesFound(devices: Collection<Device>) { }
}