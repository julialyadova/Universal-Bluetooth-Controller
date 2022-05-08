package com.example.ubc.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {
    val bluetoothEnabled = MutableLiveData<Boolean>()

    init {
        bluetoothEnabled.postValue(true)
    }

    fun switchBluetooth() {
        bluetoothEnabled.postValue(!bluetoothEnabled.value!!)
    }
}