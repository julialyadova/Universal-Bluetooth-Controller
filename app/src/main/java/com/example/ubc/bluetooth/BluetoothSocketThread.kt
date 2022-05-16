package com.example.ubc.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException

class BluetoothSocketThread(
        private val socket: BluetoothSocket,
        private val listener: BluetoothSocketListener
) : Thread() {
    private val _scanRateMS: Long = 200
    private var _active: Boolean = false

    init {
        Log.d("Bluetooth Data Thread", "bluetooth socket created")
    }

    override fun run() {
        super.run()
        try {
            this.socket.connect()
            Log.d("Bluetooth Data Thread", "connected to device - name: ${this.socket.remoteDevice.name}, address: ${this.socket.remoteDevice.address}")
        } catch (e: IOException) {
            Log.e("Bluetooth Data Thread", "run exception: " + e.message)
            listener.onConnectionInterrupted(e.message)
            return
        }

        listener.onConnectionSucceeded()
        listenSocket()
    }

    fun send(bytes: ByteArray) {
        if (this.socket.isConnected) {
            try {
                this.socket.outputStream.write(bytes)
                Log.d("Bluetooth Data Thread", "write " + String(bytes))
            } catch (e: IOException) {
                listener.onConnectionInterrupted(e.message)
                disconnect()
            }

        }
    }

    private fun listenSocket() {
        Log.i("Bluetooth Data Thread", "listening bluetooth socket on thread $name")
        val inputStream = this.socket.inputStream
        val buffer = ByteArray(1024)
        var bytesCount: Int

        //Loop to listen for received bluetooth messages
        _active = true;
        while (_active) {
            try {
                bytesCount = inputStream.read(buffer)
                Log.d("Bluetooth Data Thread", "$bytesCount bytes received")
                if (bytesCount > 0) {
                    listener.dataReceived(buffer.copyOf(bytesCount))
                }
            } catch (e: IOException) {
                Log.d("Bluetooth Data Thread", "exception while listening input data stream: " + e.message)
                e.printStackTrace()
                listener.onConnectionInterrupted(e.message)
                disconnect()
            }
            sleep(_scanRateMS)
        }
    }

    fun disconnect() {
        _active = false
        this.socket.close()
    }
}