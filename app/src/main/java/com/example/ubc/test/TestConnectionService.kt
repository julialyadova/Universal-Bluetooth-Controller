package com.example.ubc.test

import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.connection.Device

class TestConnectionService : ConnectionService() {
    private var _connection: TestConnection? = null

    override fun getAvailableDevices(): List<Device> {
        return listOf(
                Device("Test Device 1", "1"),
                Device("Test Device 2", "2"),
                Device("Test Device 3", "3"),
                Device("Test Device 4", "4")
        )
    }

    override fun getConnectedDevice(): Device? {
        return _connection?.device;
    }

    override fun getConnectionStatus(): ConnectionStatus {
        return ConnectionStatus.Connected
    }

    override fun requiredOptionIsEnabled(): Boolean {
        return true
    }

    override fun enableRequiredOption() {
        notifyStatusChanged(ConnectionStatus.RequiredOptionEnabled)
    }

    override fun disableRequiredOption() {
        notifyStatusChanged(ConnectionStatus.RequiredOptionDisabled)
    }

    override fun connect(device: Device){
        notifyStatusChanged(ConnectionStatus.Connecting)

        _connection?.disconnect()

        _connection = TestConnection(device)
        _connection!!.start()

        notifyStatusChanged(ConnectionStatus.Connected)
    }

    override fun send(data: ByteArray) {
        _connection?.send(data)
    }

    override fun send(message: String) {
        _connection?.send(message.toByteArray())
    }

    override fun disconnect() {
        _connection?.disconnect()
    }
}