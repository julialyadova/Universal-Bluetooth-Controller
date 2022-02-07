package com.example.ubc.test

import com.example.ubc.data.entities.Device

class TestBluetoothService {
    private var _connection: TestConnection? = null

    fun bluetoothEnabled() : Boolean {
        return true
    }

    fun getPairedDevices() : List<Device> {
        return listOf(
            Device("Test Device 1", "1"),
            Device("Test Device 2", "2"),
            Device("Test Device 3", "3"),
            Device("Test Device 4", "4")
        )
    }

    fun getConnection() : TestConnection? {
        return _connection
    }

    fun connect(device: Device): TestConnection? {
        _connection?.disconnect()

        _connection = TestConnection(device)
        _connection!!.start()

        return _connection
    }

    fun send(data: ByteArray) {
        _connection?.send(data)
    }

    fun disconnect() {
        _connection?.disconnect()
    }
}