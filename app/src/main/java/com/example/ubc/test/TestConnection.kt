package com.example.ubc.test


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.ubc.connection.Device

class TestConnection(
        val device: Device
) : Thread() {
    private val _logTag = "BLUETOOTH CONNECTION"
    private val _validAddress: String = "98:D3:31:F9:54:46"
    private val _scanRateMS: Long = 200
    private var received: MutableLiveData<String>? = null
    private var connected: MutableLiveData<Boolean>? = null
    private var toSend: ByteArray? = null
    private var active: Boolean = false
    val logs = mutableListOf<String>()
    private var subscribers= mutableListOf<ConnectionListener>()

    init {
        Log.d(_logTag, "connection initialized")
    }

    fun subscribe(listener: ConnectionListener) {
        subscribers.add(listener)
    }

    fun unsubscribe(listener: ConnectionListener) {
        subscribers.remove(listener)
    }

    override fun run() {
        super.run()
        //if (device.address == _validAddress) {
            log("Connected to ${device.name}")
            connected?.postValue(true)
            active = true;
        /*} else {
            connected?.postValue(false)
            log("Connection to ${device.name} failed")
        }*/
        listen()
    }

    private fun listen() {
        while (active) {
            if (toSend != null) {
                log("${device.name} received ${String(toSend!!)} <<")
                toSend = null
            }
            sleep(_scanRateMS)
        }
        log("Conection closed")
    }


    fun observe(receivedLiveData: MutableLiveData<String>, connectedLiveData: MutableLiveData<Boolean>) {
        received = receivedLiveData
        connected = connectedLiveData
    }

    fun send(bytes: ByteArray) {
        sleep(_scanRateMS)
        toSend = bytes
        log(">> ${String(bytes)}")
    }

    fun disconnect() {
        connected?.postValue(false)
        active = false
    }

    private fun log(message: String) {
        Log.d(_logTag, message)
        logs.add(message)
        received?.postValue(message)

        subscribers.forEach { s -> s.onDataReceived(message) }
    }
}