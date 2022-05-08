package com.example.ubc.bluetooth

interface BluetoothSocketListener {
    fun dataReceived(data: ByteArray)
    fun onConnectionInterrupted(error: String?)
    fun onConnectionSucceeded()
}