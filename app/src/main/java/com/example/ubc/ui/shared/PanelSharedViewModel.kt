package com.example.ubc.ui.shared

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PanelSharedViewModel @Inject constructor() : ViewModel() {
    val panelId = MutableLiveData<Int>()

    fun selectPanel(id: Int) {
        panelId.postValue(id)
    }
}