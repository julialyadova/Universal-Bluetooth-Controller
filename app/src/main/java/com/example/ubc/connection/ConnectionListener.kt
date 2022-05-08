package com.example.ubc.connection

interface ConnectionListener {
    fun onConnectionStatusChanged(target: ConnectionService, status: ConnectionStatus)
    fun onDataReceived(data: ByteArray)
}