package com.example.ubc

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.ubc.connection.ConnectionService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectionService: ConnectionService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("Activity", "connectionService: $connectionService")

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.main_activity)

        val filters = listOf<IntentFilter>(
            IntentFilter(BluetoothDevice.ACTION_FOUND),
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED),
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED),
            IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        ).forEach { filter ->
            registerReceiver(connectionService, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(connectionService)
    }
}