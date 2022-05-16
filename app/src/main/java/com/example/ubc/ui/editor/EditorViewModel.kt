package com.example.ubc.ui.editor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.ControlPanelService
import com.example.ubc.data.entities.Item
import com.example.ubc.data.entities.Panel
import com.example.ubc.items.ItemsService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val _controlPanelService: ControlPanelService,
    private val _itemIdentifierService: ItemsService
) : ViewModel() {
    val panel = MutableLiveData<Panel>()
    val items = MutableLiveData<List<Item>>()
    val itemIdentifiers = _itemIdentifierService.getItemDefinitions()

    fun init(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val p = _controlPanelService.getPanelById(id)
            val i = _controlPanelService.getItemsOfPanelWithId(id)
            withContext(Dispatchers.Main) {
                panel.value = p
                items.value = i
                Log.d("ItemsViewModel", "items loaded: ${i.size}")
            }
        }
    }

    fun createItem(type: String) {
        Log.d("ItemsViewModel", "create item $type")
        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.addItemToPanel(type, panel.value!!.id)
            val i = _controlPanelService.getItemsOfPanelWithId(panel.value!!.id)
            withContext(Dispatchers.Main) {
                items.value = i
            }
        }
    }

    fun update(item: Item) {
        Log.d("ItemsViewModel", "save item ${item.id} ${item.label}")
        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.updateItem(item)
            withContext(Dispatchers.Main) {
                items.value = items.value
            }
        }
    }

    fun setPosition(id: Int, x: Int, y: Int) {
        Log.d("ItemsViewModel", "move item $id to ($x; $y)")
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
        Log.d("ItemsViewModel", "delete item $itemId")
        GlobalScope.launch(Dispatchers.IO) {
            _controlPanelService.deleteItem(itemId)
            val i = _controlPanelService.getItemsOfPanelWithId(panel.value!!.id)
            withContext(Dispatchers.Main) {
                items.value = i
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