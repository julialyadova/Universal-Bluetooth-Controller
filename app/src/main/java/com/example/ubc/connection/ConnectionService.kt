package com.example.ubc.connection

import android.util.Log
import javax.inject.Singleton

@Singleton
abstract class ConnectionService {
    private var listeners: MutableList<ConnectionListener> = mutableListOf()

    fun subscribe(listener: ConnectionListener){
        listeners.add(listener)
        Log.d("ConnectionService", "new subscriber added to instance $this. total ${listeners.size} active listeners")
    }

    fun unsubscribe(listener: ConnectionListener) {
        listeners.remove((listener))
        Log.d("ConnectionService", "subscriber removed from instance $this. total ${listeners.size} active listeners")
    }

    protected fun notifyStatusChanged(status: ConnectionStatus, device: Device? = null) {
        Log.d("Connection Service", "status changed: $status")
        for (listener in listeners) {
            listener.onConnectionStatusChanged(status, device ?: getConnectedDevice());
        }
    }

    protected fun notifyDataReceived(data: ByteArray) {
        Log.d("Connection Service", "data received: $data")
        for (listener in listeners) {
            listener.onDataReceived(data);
        }
    }

    abstract fun getAvailableDevices() : List<Device>
    abstract fun getConnectedDevice() : Device?
    abstract fun getConnectionStatus() : ConnectionStatus
    abstract fun requiredOptionIsEnabled() : Boolean
    abstract fun enableRequiredOption()
    abstract fun disableRequiredOption()
    abstract fun connect(device: Device)
    abstract fun disconnect()
    abstract fun send(bytes: ByteArray)
    abstract fun send(message: String)
}