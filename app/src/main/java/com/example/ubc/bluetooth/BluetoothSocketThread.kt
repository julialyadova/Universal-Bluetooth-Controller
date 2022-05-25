package com.example.ubc.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException

class BluetoothSocketThread(
        private val socket: BluetoothSocket,
        private val listener: BluetoothSocketListener
) : Thread() {
    private val _scanRateMS: Long = 200
    private val _shortScanRateMS: Long = 10
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
        val message = ByteArray(1024)
        val buffer = ByteArray(256)
        var messageLength = 0
        var bytesRead: Int

        //Loop to listen for received bluetooth messages
        _active = true;
        while (_active) {
            try {
                while(inputStream.available() > 0) {
                    bytesRead = inputStream.read(buffer)
                    Log.d("Bluetooth Data Thread", "bytes received: $bytesRead")
                    buffer.copyInto(message, messageLength, 0, bytesRead)
                    messageLength += bytesRead
                    sleep(_shortScanRateMS)
                }

                if (messageLength > 0) {
                    Log.d("Bluetooth Data Thread", "message received: $messageLength")
                    listener.dataReceived(message.copyOf(messageLength))
                    messageLength = 0
                }
            } catch (e: IOException) {
                Log.d("Bluetooth Data Thread", "exception while listening input data stream: " + e.message)
                e.printStackTrace()
                listener.onConnectionInterrupted(e.message)
                disconnect()
            }
            sleep(_scanRateMS*8)
        }
    }

    fun disconnect() {
        _active = false
        this.socket.close()
    }
}