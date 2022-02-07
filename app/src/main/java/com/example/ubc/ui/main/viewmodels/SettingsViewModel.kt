package com.example.ubc.ui.main.viewmodels

import android.bluetooth.BluetoothAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
        private var bluetoothAdapter: BluetoothAdapter
) : ViewModel() {
    val bluetoothEnabled = MutableLiveData<Boolean>()

    init {
        bluetoothEnabled.postValue(bluetoothAdapter.isEnabled)
    }

    fun switchBluetooth() {
        if (bluetoothAdapter.isEnabled) {
            bluetoothAdapter.disable()
        } else {
            bluetoothAdapter.enable()
        }

        bluetoothEnabled.postValue(bluetoothAdapter.isEnabled)
    }
}