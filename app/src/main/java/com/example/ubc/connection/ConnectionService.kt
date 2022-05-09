package com.example.ubc.connection

import android.content.BroadcastReceiver
import android.util.Log
import javax.inject.Singleton

@Singleton
abstract class ConnectionService : BroadcastReceiver() {
    private var listeners: MutableList<ConnectionListener> = mutableListOf()

    fun subscribe(listener: ConnectionListener){
        listeners.add(listener)
        Log.d("ConnectionService", "new subscriber added to instance $this. total ${listeners.size} active listeners")
    }

    fun unsubscribe(listener: ConnectionListener) {
        listeners.remove((listener))
        Log.d("ConnectionService", "subscriber removed from instance $this. total ${listeners.size} active listeners")
    }

    protected fun notifyStatusChanged(state: ConnectionState, device: Device? = null) {
        Log.d("Connection Service", "status changed: $state")
        for (listener in listeners) {
            listener.onConnectionStatusChanged(state, device ?: getConnectedDevice());
        }
    }

    protected fun notifyAdapterStateChanged(state: AdapterState) {
        Log.d("Connection Service", "adapter state changed: $state")
        for (listener in listeners) {
            listener.onAdapterStateChanged(state);
        }
    }

    protected fun notifyDataReceived(data: ByteArray) {
        Log.d("Connection Service", "data received: $data")
        for (listener in listeners) {
            listener.onDataReceived(data);
        }
    }

    protected fun notifyDevicesFound(devices: Collection<Device>) {
        Log.d("Connection Service", "devices found: ${devices.size}")
        for (listener in listeners) {
            listener.onAvailableDevicesFound(devices);
        }
    }

    abstract fun scanForAvailableDevices()
    abstract fun cancelScanning()
    abstract fun getConnectedDevice() : Device?
    abstract fun getConnectionStatus() : ConnectionState
    abstract fun adapterEnabled() : Boolean
    abstract fun enableRequiredOption()
    abstract fun disableRequiredOption()
    abstract fun connect(device: Device)
    abstract fun disconnect()
    abstract fun send(bytes: ByteArray)
    abstract fun send(message: String)
}