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
            return
        }

        listenSocket()
    }

    fun send(bytes: ByteArray) {
        if (this.socket.isConnected) {
            this.socket.outputStream.write(bytes)
            Log.d("Bluetooth Data Thread", "write " + String(bytes))
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
                val readMessage = String(buffer, 0, bytesCount)
                Log.d("Bluetooth Data Thread", "$bytesCount bytes received")
                if (bytesCount > 0) {
                    listener.dataReceived(buffer.copyOf(bytesCount))
                }
            } catch (e: IOException) {
                Log.d("Bluetooth Data Thread", "exception while listening input data stream: " + e.message)
                e.printStackTrace()
                if (!this.socket.isConnected) {
                    this.socket.close()
                    listener.onConnectionInterrupted(e.message)
                    break
                }
            }
            sleep(_scanRateMS)
        }
    }

    fun disconnect() {
        _active = false
        this.socket.close()
    }
}