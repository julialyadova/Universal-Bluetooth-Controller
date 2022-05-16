package com.example.ubc.test

import android.content.Context
import android.content.Intent
import com.example.ubc.connection.AdapterState
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionState
import com.example.ubc.connection.Device

class TestConnectionService : ConnectionService() {
    private var connectedDevice: Device? = null
    private var adapterEnabled: Boolean = true

    override fun getAdapterName(): String {
        return "Test"
    }

    override fun scanForAvailableDevices() {
        notifyAdapterStateChanged(AdapterState.StartedScanning)
        Thread.sleep(500)
        notifyDevicesFound(listOf(
                Device("Test Device 1", "1"),
                Device("Test Device 2", "2"),
                Device("Test Device 3", "3"),
                Device("Test Device 4", "4")
        ))
        notifyAdapterStateChanged(AdapterState.FinishedScanning)
    }

    override fun cancelScanning() {
        notifyAdapterStateChanged(AdapterState.FinishedScanning)
    }

    override fun getConnectedDevice(): Device? {
        return connectedDevice
    }

    override fun getConnectionStatus(): ConnectionState {
        return if (connectedDevice == null)
            ConnectionState.Disconnected
        else
            ConnectionState.Connected
    }

    override fun adapterEnabled(): Boolean {
        return adapterEnabled
    }

    override fun enableRequiredOption() {
        adapterEnabled = true
        notifyAdapterStateChanged(AdapterState.Enabled)
    }

    override fun disableRequiredOption() {
        adapterEnabled = false
        notifyAdapterStateChanged(AdapterState.FinishedScanning)
        disconnect()
        notifyAdapterStateChanged(AdapterState.Disabled)
    }

    override fun connect(device: Device){
        notifyStatusChanged(ConnectionState.Connecting, device)

        Thread.sleep(500)
        connectedDevice = device

        notifyStatusChanged(ConnectionState.Connected, device)
    }

    override fun send(bytes: ByteArray) {
        notifyDataReceived(bytes)
    }

    override fun send(message: String) {
        notifyDataReceived(message.toByteArray())
    }

    override fun onReceive(context: Context?, intent: Intent?) { }

    override fun disconnect() {
        notifyStatusChanged(ConnectionState.Disconnected, connectedDevice)
        connectedDevice = null
    }
}