package com.example.ubc.connection

interface ConnectionListener {
    fun onConnectionStatusChanged(status: ConnectionStatus, device: Device?)
    fun onDataReceived(data: ByteArray)
}