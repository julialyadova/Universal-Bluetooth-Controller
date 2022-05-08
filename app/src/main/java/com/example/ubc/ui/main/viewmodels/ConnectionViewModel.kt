package com.example.ubc.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.connection.ConnectionListener
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.connection.Device
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
        private var connectionService: ConnectionService
) : ViewModel(), ConnectionListener {
    val requiredOptionEnabled = MutableLiveData<Boolean>()
    val availableDevices = MutableLiveData<List<Device>>()
    val activeDevice = MutableLiveData<Device?>()
    val connected = MutableLiveData<Boolean>()
    val device = MutableLiveData<Device>()
    val received = MutableLiveData<String>()

    init {
        connectionService.subscribe(this)
    }

    override fun onCleared() {
        connectionService.unsubscribe(this)
        super.onCleared()
    }

    fun findDevices() {
        availableDevices.postValue(connectionService.getAvailableDevices())
    }

    fun onDeviceClicked(device: Device) {
        if (device == activeDevice.value) {
            connectionService.disconnect()
            activeDevice.postValue(null)
            connected.postValue(false)
        } else {
            connect(device)
        }
    }

    fun send(data: String) {
        connectionService.send(data)
    }

    private fun connect(device: Device) {
        connectionService.connect(device)
    }

    override fun onConnectionStatusChanged(target: ConnectionService, status: ConnectionStatus) {
        if (status == ConnectionStatus.Connected) {
            activeDevice.postValue(target.getConnectedDevice())
            connected.postValue(true)
        } else if (status == ConnectionStatus.Disconnected) {
           activeDevice.postValue(null)
           connected.postValue(false)
        } else if (status == ConnectionStatus.RequiredOptionDisabled) {
            requiredOptionEnabled.postValue(false);
            connected.postValue(false)
        } else if (status == ConnectionStatus.RequiredOptionEnabled) {
            requiredOptionEnabled.postValue(true);
        }
    }

    override fun onDataReceived(data: ByteArray) {
        received.postValue(data.toString())
    }
}