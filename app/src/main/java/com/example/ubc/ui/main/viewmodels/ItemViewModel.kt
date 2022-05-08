package com.example.ubc.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ItemViewModel : ViewModel() {
    val received = MutableLiveData<ByteArray>()

    abstract fun send(data: ByteArray)
}