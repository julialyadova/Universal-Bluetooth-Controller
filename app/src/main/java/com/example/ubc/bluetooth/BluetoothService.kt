package com.example.ubc.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.ubc.connection.AdapterState
import com.example.ubc.connection.ConnectionService
import com.example.ubc.connection.ConnectionState
import com.example.ubc.connection.Device
import java.util.*
import javax.inject.Singleton

@Singleton
class BluetoothService : ConnectionService(), BluetoothSocketListener {
    private var _adapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val _uuid: String = "00001101-0000-1000-8000-00805f9b34fb"
    private var _socket: BluetoothSocket? = null
    private var _activeSocketThread: BluetoothSocketThread? = null
    private val _availableDevices: MutableMap<String,Device> = mutableMapOf()

    override fun getAdapterName(): String {
        return "Bluetooth"
    }

    override fun scanForAvailableDevices() {
        if (_adapter.isEnabled && !_adapter.isDiscovering) {
            _availableDevices.clear()
            for (device in _adapter.bondedDevices) {
                _availableDevices[device.address] = Device(device.name, device.address, "paired")
            }
            _activeSocketThread?.disconnect()
            _adapter.startDiscovery()
        }
    }

    override fun cancelScanning() {
        if (_adapter.isDiscovering)
            _adapter.cancelDiscovery()
    }

    override fun getConnectedDevice(): Device? {
        val bluetoothDevice = _socket?.remoteDevice
        return if (_socket?.isConnected != true || bluetoothDevice == null)
            null
        else
            Device(bluetoothDevice.name, bluetoothDevice.address)
    }

    override fun getConnectionStatus(): ConnectionState {
        return if (_adapter.isEnabled)
            ConnectionState.Connected
        else
            ConnectionState.Disconnected
    }

    override fun adapterEnabled(): Boolean {
        return _adapter.isEnabled
    }

    override fun enableRequiredOption() {
        _adapter.enable()
    }

    override fun disableRequiredOption() {
        if (_adapter.isDiscovering)
            _adapter.cancelDiscovery()
        _adapter.disable()
    }

    override fun connect(device: Device) {
        notifyStatusChanged(ConnectionState.Connecting, device)

        if (!_adapter.isEnabled) {
            notifyStatusChanged(ConnectionState.Disconnected, device)
            return
        }

        if (_adapter.isDiscovering) {
            _adapter.cancelDiscovery()
        }

        val bluetoothDevice = _adapter.getRemoteDevice(device.address)
        if (bluetoothDevice == null) {
            notifyStatusChanged(ConnectionState.Disconnected, device)
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
        notifyStatusChanged(ConnectionState.Disconnecting, tryGetRemoteDevice())
        _activeSocketThread?.disconnect()
        _socket?.close()
        notifyStatusChanged(ConnectionState.Disconnected, tryGetRemoteDevice())
    }

    override fun send(bytes: ByteArray) {
        _activeSocketThread?.send(bytes)
    }

    override fun send(message: String) {
        _activeSocketThread?.send(message.toByteArray())
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null)
            return
        Log.d("Bluetooth Service", "onReceive: ${intent.action}")
        when(intent.action) {
            BluetoothDevice.ACTION_FOUND -> {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    if (_availableDevices[device.address]?.name == null) {
                        _availableDevices[device.address] = Device(device.name, device.address)
                        notifyDevicesFound(_availableDevices.map { entry -> entry.value })
                    }
                }
            }
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)
                if (state == BluetoothAdapter.STATE_ON)
                    notifyAdapterStateChanged(AdapterState.Enabled)
                else if (state == BluetoothAdapter.STATE_OFF)
                    notifyAdapterStateChanged(AdapterState.Disabled)
            }
            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                notifyAdapterStateChanged(AdapterState.StartedScanning)
            }
            BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                notifyAdapterStateChanged(AdapterState.FinishedScanning)
            }
        }
    }

    override fun dataReceived(data: ByteArray) {
        notifyDataReceived(data)
    }

    override fun onConnectionInterrupted(error: String?) {
        notifyStatusChanged(ConnectionState.Disconnected, tryGetRemoteDevice())
    }

    override fun onConnectionSucceeded() {
        notifyStatusChanged(ConnectionState.Connected, tryGetRemoteDevice())
    }

    private fun tryGetRemoteDevice() : Device? {
        var device : Device? = null
        _socket?.remoteDevice?.let {
            device = Device(it.name, it.address)
        }
        return device
    }
}