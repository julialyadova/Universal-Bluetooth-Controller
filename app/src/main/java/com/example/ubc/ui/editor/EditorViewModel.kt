package com.example.ubc.ui.editor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.ControlPanelService
import com.example.ubc.items.Item
import com.example.ubc.items.ItemDefinition
import com.example.ubc.items.Panel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val _controlPanelService: ControlPanelService
) : ViewModel() {
    val panel = MutableLiveData<Panel>()
    val items = MutableLiveData<List<Item>>()
    val itemDefinitions = ItemDefinition.definitions

    fun init(id: Int) {
        log("init($id)")
        GlobalScope.launch(Dispatchers.IO) {
            val p = _controlPanelService.getPanelById(id)
            val i = _controlPanelService.getItemsOfPanelWithId(id)
            withContext(Dispatchers.Main) {
                panel.value = p
                items.value = i
                log("loaded panel: ${p.id} ${p.name} with ${i.size} items")
            }
        }
    }

    fun createItem(type: String) {
        log("createItem($type)")

        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.addItemToPanel(type, panel.value!!.id)
            val i = _controlPanelService.getItemsOfPanelWithId(panel.value!!.id)
            withContext(Dispatchers.Main) {
                items.value = i
            }
        }
    }

    fun update(item: Item) {
        log("update(item ${item.id})")

        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.updateItem(item)
            withContext(Dispatchers.Main) {
                items.value = items.value
            }
        }
    }

    fun setPosition(id: Int, x: Int, y: Int) {
        log("setPosition(item=$id, x=$x, y=$y)")

        GlobalScope.launch(Dispatchers.IO) {
            val item = items.value?.find { i -> i.id == id }
            if (item != null) {
                item.x = x.toFloat()
                item.y = y.toFloat()
                _controlPanelService.updateItem(item)
                withContext(Dispatchers.Main) {
                    items.value = items.value
                }
            }
        }
    }

    fun delete(itemId: Int) {
        log("delete(item=$itemId)")

        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.deleteItem(itemId)
            val i = _controlPanelService.getItemsOfPanelWithId(panel.value!!.id)
            withContext(Dispatchers.Main) {
                items.value = i
            }
        }
    }

    fun renamePanel(name: String) {
        log("renamePanel($name)")

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

    private fun log(message: String) {
        Log.d("ItemsViewModel", message)
    }
}