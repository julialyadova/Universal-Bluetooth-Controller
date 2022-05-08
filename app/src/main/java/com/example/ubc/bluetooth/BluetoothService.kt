package com.example.ubc.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionStatus
import com.example.ubc.connection.Device
import java.util.*
import javax.inject.Singleton

@Singleton
class BluetoothService : ConnectionService(), BluetoothSocketListener {
    private var _adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val _uuid: String = "00001101-0000-1000-8000-00805f9b34fb"
    private var _socket: BluetoothSocket? = null
    private var _activeSocketThread: BluetoothSocketThread? = null

    override fun getAvailableDevices(): List<Device> {
        return _adapter.bondedDevices.map { d -> Device(d.name, d.address) }
    }

    override fun getConnectedDevice(): Device? {
        val bluetoothDevice = _socket?.remoteDevice
        return if (_socket?.isConnected != true || bluetoothDevice == null)
            null
        else
            Device(bluetoothDevice.name, bluetoothDevice.address)
    }

    override fun getConnectionStatus(): ConnectionStatus {
        return if (_adapter.isEnabled)
            ConnectionStatus.Connected
        else
            ConnectionStatus.Disconnected
    }

    override fun requiredOptionIsEnabled(): Boolean {
        return _adapter.isEnabled
    }

    override fun enableRequiredOption() {
        _adapter.enable()
        notifyStatusChanged(ConnectionStatus.RequiredOptionEnabled)
    }

    override fun disableRequiredOption() {
        _adapter.disable()
        notifyStatusChanged(ConnectionStatus.RequiredOptionDisabled)
    }

    override fun connect(device: Device) {
        notifyStatusChanged(ConnectionStatus.Connecting, device)

        if (!_adapter.isEnabled) {
            notifyStatusChanged(ConnectionStatus.RequiredOptionDisabled)
            return
        }

        val bluetoothDevice = _adapter.getRemoteDevice(device.address)
        if (bluetoothDevice == null) {
            notifyStatusChanged(ConnectionStatus.Disconnected, device)
            return
        }

        _socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(_uuid))
        Log.d("Bluetooth Service", "bluetooth socket created")

        if (_activeSocketThread != null) {
            _activeSocketThread!!.disconnect()
        }

        _activeSocketThread = BluetoothSocketThread(_socket!!, this)
        _activeSocketThread!!.start()
    }

    override fun disconnect() {
        notifyStatusChanged(ConnectionStatus.Disconnecting, tryGetRemoteDevice())
        _activeSocketThread?.disconnect()
        _socket?.close()
        notifyStatusChanged(ConnectionStatus.Disconnected, tryGetRemoteDevice())
    }

    override fun send(bytes: ByteArray) {
        _activeSocketThread?.send(bytes)
    }

    override fun send(message: String) {
        _activeSocketThread?.send(message.toByteArray())
    }

    override fun dataReceived(data: ByteArray) {
        notifyDataReceived(data)
    }

    override fun onConnectionInterrupted(error: String?) {
        notifyStatusChanged(ConnectionStatus.Disconnected, tryGetRemoteDevice())
    }

    override fun onConnectionSucceeded() {
        notifyStatusChanged(ConnectionStatus.Connected, tryGetRemoteDevice())
    }

    private fun tryGetRemoteDevice() : Device? {
        var device : Device? = null
        _socket?.remoteDevice?.let {
            device = Device(it.name, it.address)
        }
        return device
    }
}