package com.example.ubc.ui.menu

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.entities.PanelEntity
import com.example.ubc.data.repositories.PanelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private var panels: PanelsRepository
) : ViewModel() {
    val panelsList = MutableLiveData<List<PanelEntity>>()
    val newlyCreatedPanelId = MutableLiveData<Int>()

    fun loadMenu() {
        GlobalScope.launch(Dispatchers.IO) {
            val panels = panels.getAll()
            withContext(Dispatchers.Main) {
                panelsList.value = panels
            }
        }
    }

    fun deletePanel(panel: PanelEntity){
        GlobalScope.launch(Dispatchers.IO) {
            panels.delete(panel)
            val panels = panels.getAll()
            withContext(Dispatchers.Main) {
                panelsList.postValue(panels)
            }
        }
    }

    fun createPanel(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = panels.add(name)
            withContext(Dispatchers.Main) {
                newlyCreatedPanelId.value = id
            }
        }
    }
}