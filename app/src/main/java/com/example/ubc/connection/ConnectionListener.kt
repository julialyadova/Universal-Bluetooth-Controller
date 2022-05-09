package com.example.ubc.connection

interface ConnectionListener {
    fun onConnectionStatusChanged(state: ConnectionState, device: Device?)
    fun onAdapterStateChanged(state: AdapterState)
    fun onDataReceived(data: ByteArray)
    fun onAvailableDevicesFound(devices: Collection<Device>)
}