package com.example.ubc.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import com.example.ubc.data.entities.Device

class BluetoothService {
    private var _adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private var _connection: Connection? = null

    fun bluetoothEnabled() : Boolean {
        return _adapter.isEnabled
    }

    fun getPairedDevices() : List<Device> {
        return _adapter.bondedDevices.map { d -> Device(d.name, d.address) }
    }

    fun getConnectedDevice() : BluetoothDevice? {
        return _connection?.device
    }

    fun connect(device: Device): Connection? {
        val bluetoothDevice = _adapter.getRemoteDevice(device.address)

        _connection?.disconnect()

        if (_adapter.isEnabled && bluetoothDevice != null) {
            _connection = Connection(bluetoothDevice)
            _connection!!.start()
        }

        return _connection
    }

    fun disconnect() {
        _connection?.disconnect()
    }
}