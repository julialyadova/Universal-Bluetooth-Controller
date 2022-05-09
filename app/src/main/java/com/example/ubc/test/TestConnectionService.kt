package com.example.ubc.test

import android.content.Context
import android.content.Intent
import com.example.ubc.connection.AdapterState
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionState
import com.example.ubc.connection.Device

class TestConnectionService : ConnectionService() {
    private var _connection: TestConnection? = null

    override fun scanForAvailableDevices() {
        notifyDevicesFound(listOf(
                Device("Test Device 1", "1"),
                Device("Test Device 2", "2"),
                Device("Test Device 3", "3"),
                Device("Test Device 4", "4")
        ))
    }

    override fun cancelScanning() {
        TODO("Not yet implemented")
    }

    override fun getConnectedDevice(): Device? {
        return _connection?.device;
    }

    override fun getConnectionStatus(): ConnectionState {
        return ConnectionState.Connected
    }

    override fun adapterEnabled(): Boolean {
        return true
    }

    override fun enableRequiredOption() {
        notifyAdapterStateChanged(AdapterState.Enabled)
    }

    override fun disableRequiredOption() {
        notifyAdapterStateChanged(AdapterState.Disabled)
    }

    override fun connect(device: Device){
        notifyStatusChanged(ConnectionState.Connecting)

        _connection?.disconnect()

        _connection = TestConnection(device)
        _connection!!.start()

        notifyStatusChanged(ConnectionState.Connected)
    }

    override fun send(data: ByteArray) {
        _connection?.send(data)
    }

    override fun send(message: String) {
        _connection?.send(message.toByteArray())
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        _connection?.disconnect()
    }
}