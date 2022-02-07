package com.example.ubc.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.entities.Device
import com.example.ubc.test.TestBluetoothService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
        private var bluetoothService: TestBluetoothService
) : ViewModel() {
    val bluetoothEnabled = MutableLiveData<Boolean>()
    val availableDevices = MutableLiveData<List<Device>>()
    val activeDevice = MutableLiveData<Device?>()
    val connected = MutableLiveData<Boolean>()
    val device = MutableLiveData<Device>()
    val log = MutableLiveData<String>()
    val history = MutableLiveData<List<String>>()

    init {
        bluetoothEnabled.postValue(bluetoothService.bluetoothEnabled())
    }

    fun findDevices() {
        availableDevices.postValue(bluetoothService.getPairedDevices())
    }

    fun loadLogs() {
        history.postValue(bluetoothService.getConnection()?.logs ?: emptyList())
    }

    fun clearLogs() {
        bluetoothService.getConnection()?.logs?.clear()
        history.postValue(emptyList())
    }

    fun getLogsAsText() : String {
        return bluetoothService.getConnection()?.logs?.joinToString("\r\n") ?: ""
    }

    fun deviceClick(device: Device) {
        if (device.address == activeDevice.value?.address) {
            bluetoothService.disconnect()
            activeDevice.postValue(null)
            connected.postValue(false)
        } else {
            connect(device)
        }
    }

    fun send(data: String) {
        log.postValue(">> $data")
        bluetoothService.send(data.toByteArray())
    }

    private fun connect(device: Device) {
        val connection = bluetoothService.connect(device)
        if (connection != null) {
            connection.observe(log, connected)
            activeDevice.postValue(device)
            log.postValue("Connected to ${device.name}")
        } else {
            connected.postValue(false)
            log.postValue("failed to connect")
        }
    }

}