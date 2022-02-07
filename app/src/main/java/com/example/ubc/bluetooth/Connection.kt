package com.example.ubc.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.io.IOException
import java.util.*

class Connection(
        val device: BluetoothDevice
) : Thread() {
    private val _logTag = "BLUETOOTH CONNECTION"
    private val _uuid: String = "00001101-0000-1000-8000-00805f9b34fb"
    private val _scanRateMS: Long = 200
    private val _socket: BluetoothSocket
    private var received: MutableLiveData<ByteArray>? = null
    private var connected: MutableLiveData<Boolean>? = null

    init {
        _socket = device.createRfcommSocketToServiceRecord(UUID.fromString(_uuid))
        Log.d(_logTag, "bluetooth socket created")
    }

    override fun run() {
        super.run()
        try {
            _socket.connect()
            connected?.postValue(true)
            Log.d(_logTag, "connected to device - name: ${_socket.remoteDevice.name}, address: ${_socket.remoteDevice.address}")
        } catch (e: IOException) {
            Log.e(_logTag, "run exception: " + e.message)
            return
        }

        listenSocket()
    }

    fun observe(receivedLiveData: MutableLiveData<ByteArray>, connectedLiveData: MutableLiveData<Boolean>) {
        received = receivedLiveData
        connected = connectedLiveData
    }

    fun send(bytes: ByteArray) {
        if (_socket.isConnected) {
            _socket.outputStream.write(bytes)
            Log.d(_logTag, "write " + String(bytes))
        }
    }

    private fun listenSocket() {
        Log.i(_logTag, "listening bluetooth socket on thread $name")
        val inputStream = _socket.inputStream
        val buffer = ByteArray(1024)
        var bytesCount: Int

        //Loop to listen for received bluetooth messages
        while (true) {
            try {
                bytesCount = inputStream.read(buffer)
                val readMessage = String(buffer, 0, bytesCount)
                Log.d(_logTag, "$bytesCount bytes received")
                if (bytesCount > 0)
                    received?.postValue(buffer.copyOf(bytesCount))
            } catch (e: IOException) {
                Log.d(_logTag, "exception while listening input data stream: " + e.message)
                e.printStackTrace()
                if (!_socket.isConnected) {
                    connected?.postValue(false)
                    break
                }
            }
            sleep(_scanRateMS)
        }
    }

    fun disconnect() {
        _socket.close()
        connected?.postValue(false)
    }
}