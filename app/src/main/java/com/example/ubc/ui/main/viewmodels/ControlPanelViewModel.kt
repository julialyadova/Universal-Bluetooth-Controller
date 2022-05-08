package com.example.ubc.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ubc.connection.ConnectionListener
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.connection.Device
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
    val device = MutableLiveData<String>()
    val deviceStatus = MutableLiveData<ConnectionStatus>()

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
                if (connectedDevice == null) {
                    device.value = "не соединено"
                } else {
                    device.value = connectedDevice.name ?: connectedDevice.address
                    deviceStatus.value = _connectionService.getConnectionStatus()
                }
            }
        }
    }

    override fun send(data: ByteArray) {
        Log.d("Control Panel ViewModel", "send: $data")
    }

    override fun onConnectionStatusChanged(status: ConnectionStatus, targetDevice: Device?) {
        Log.d("ControlPanelViewModel", "onConnectionStatusChanged: $status ${targetDevice?.address}")
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                if (targetDevice != null) {
                    device.value = targetDevice.name ?: "Устройстов без имени"
                    deviceStatus.value = status
                }
            }
        }
    }

    override fun onDataReceived(data: ByteArray) {
        received.value = data
    }
}