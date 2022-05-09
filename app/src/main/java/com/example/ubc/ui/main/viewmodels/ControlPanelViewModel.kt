package com.example.ubc.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ubc.connection.*
import com.example.ubc.data.ControlPanelService
import com.example.ubc.data.entities.Item
import com.example.ubc.data.entities.Panel
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
) : ItemViewModel(), ConnectionListener {
    val panel = MutableLiveData<Panel>()
    val items = MutableLiveData<List<Item>>()
    val device = MutableLiveData<String?>()
    val deviceStatus = MutableLiveData<ConnectionState>()

    init {
        _connectionService.subscribe(this)
    }

    override fun onCleared() {
        _connectionService.unsubscribe(this)
    }

    fun load(id: Int) {
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
            }
        }
    }

    override fun send(data: ByteArray) {
        Log.d("Control Panel ViewModel", "send: $data")
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
            device.value = "! адаптер выключен"
            deviceStatus.value = ConnectionState.Disconnected
        } else {
            device.value = "соединение не установлено"
        }
    }

    override fun onDataReceived(data: ByteArray) {
        received.value = data
    }

    override fun onAvailableDevicesFound(devices: Collection<Device>) { }
}