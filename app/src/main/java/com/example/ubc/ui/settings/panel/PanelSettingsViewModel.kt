package com.example.ubc.ui.settings.panel

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
class PanelSettingsViewModel @Inject constructor(
    private val _controlPanelService: ControlPanelService
) : ViewModel() {
    val panel = MutableLiveData<Panel>()
    val panelDeleted = MutableLiveData<Boolean>(false)


    fun load(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            if (!_controlPanelService.panelExists(id))
                return@launch

            val panelData = _controlPanelService.getPanelById(id)
            withContext(Dispatchers.Main) {
                panel.value = panelData
            }
        }
    }

    fun delete() {
        GlobalScope.launch(Dispatchers.IO) {
            if (panel.value != null)
                _controlPanelService.deletePanel(panel.value!!.id)
            withContext(Dispatchers.Main) {
                panelDeleted.value = true
            }
        }
    }

    fun renamePanel(name: String) {
        val panelId = panel.value?.id
        if (panelId != null) {
            GlobalScope.launch(Dispatchers.IO) {
                _controlPanelService.renamePanel(panelId, name)
                val p = _controlPanelService.getPanelById(panelId)
                withContext(Dispatchers.Main) {
                    panel.value = p
                }
            }
        }
    }
}