package com.example.ubc.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.ControlPanelService
import com.example.ubc.items.Panel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private var _controlPanelService: ControlPanelService
) : ViewModel() {
    val panelsList = MutableLiveData<List<Panel>>()
    val newlyCreatedPanelId = MutableLiveData<Int>()

    fun loadMenu() {
        GlobalScope.launch(Dispatchers.IO) {
            val panels = _controlPanelService.getAllPanels()
            withContext(Dispatchers.Main) {
                panelsList.value = panels
            }
        }
    }

    fun deletePanel(panel: Panel){
        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.deletePanel(panel.id)
            val panels = _controlPanelService.getAllPanels()
            withContext(Dispatchers.Main) {
                panelsList.value = panels
            }
        }
    }

    fun createPanel(name: String, isHorizontal: Boolean) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = _controlPanelService.createPanel(name, isHorizontal)
            withContext(Dispatchers.Main) {
                newlyCreatedPanelId.value = id
            }
        }
    }
}