package com.example.ubc.connection

abstract class ConnectionService {
    private var listeners: MutableList<ConnectionListener> = mutableListOf()

    fun subscribe(listener: ConnectionListener){
        listeners.add(listener)
    }

    fun unsubscribe(listener: ConnectionListener) {
        listeners.remove((listener))
    }

    protected fun notifyStatusChanged(status: ConnectionStatus) {
        for (listener in listeners) {
            listener.onConnectionStatusChanged(this,status);
        }
    }

    protected fun notifyDataReceived(data: ByteArray) {
        for (listener in listeners) {
            listener.onDataReceived(data);
        }
    }

    abstract fun getAvailableDevices() : List<Device>
    abstract fun getConnectedDevice() : Device?
    abstract fun getConnectionStatus() : ConnectionStatus
    abstract fun connect(device: Device)
    abstract fun disconnect()
    abstract fun send(bytes: ByteArray)
    abstract fun send(message: String)
}