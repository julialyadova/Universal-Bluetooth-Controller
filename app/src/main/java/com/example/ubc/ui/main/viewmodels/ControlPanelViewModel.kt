package com.example.ubc.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.entities.Control
import com.example.ubc.data.entities.Panel
import com.example.ubc.data.repositories.ControlPanelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ControlPanelViewModel @Inject constructor(
    private val panels: ControlPanelRepository,
) : ViewModel() {
    val panelData = MutableLiveData<Panel>()
    val controlsData = MutableLiveData<List<Control>>()

    fun loadPanel(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val panel = panels.getById(id)
            val controls = panels.getControls(id)
            withContext(Dispatchers.Main) {
                panelData.value = panel
                controlsData.value = controls
            }
        }
    }

    fun createPanel(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = panels.add(name)
            val panel = panels.getById(id)
            val controls = panels.getControls(id)
            withContext(Dispatchers.Main) {
                panelData.value = panel
                controlsData.value = controls
            }
        }
    }

    fun renamePanel(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val panel = panelData.value!!
            panel.name = name;
            panels.update(panel)
            withContext(Dispatchers.Main) {
                panelData.value = panel
            }
        }
    }

    fun saveControl(control: Control) {
        GlobalScope.launch(Dispatchers.IO) {
            control.panelId = panelData.value!!.id
            panels.saveControl(control)
            withContext(Dispatchers.Main) {
                loadControls()
            }
        }
    }

    fun deleteControl(control: Control) {
        GlobalScope.launch(Dispatchers.IO) {
            panels.deleteControl(control)
            withContext(Dispatchers.Main) {
                loadControls()
            }
        }
    }

    private fun loadControls() {
        GlobalScope.launch(Dispatchers.IO) {
            val controls = panels.getControls(panelData.value!!.id)
            withContext(Dispatchers.Main) {
                controlsData.value = controls
            }
        }
    }
}