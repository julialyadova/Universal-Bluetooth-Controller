package com.example.ubc.data

import android.util.Log
import com.example.ubc.data.entities.Item
import com.example.ubc.data.entities.Panel
import com.example.ubc.data.repositories.ItemsRepository
import com.example.ubc.data.repositories.PanelsRepository
import javax.inject.Inject

class ControlPanelService @Inject constructor(
        private val _items : ItemsRepository,
        private val _panels: PanelsRepository
) {
    suspend fun getPanelById(id: Int) : Panel {
        return _panels.getById(id)
    }

    suspend fun getItemsOfPanelWithId(id: Int) : List<Item> {
        return  _items.findByPanelId(id)
    }

    suspend fun renamePanel(id: Int, name: String) {
        val panel = _panels.getById(id)
        panel.name = name
        _panels.update(panel)
    }

    suspend fun addItemToPanel(type: String, panelId: Int) : Item {
        val item = Item(0, panelId, "label", type, "", Item.DataFormats.ASCII)
        _items.insert(item)
        Log.d("ControlPanelService", "added new item: ${item.id} ${item.label}")
        return item
    }

    suspend fun deleteItem(id: Int) {
        val item = _items.getById(id)
        if (item != null) {
            _items.delete(item)
        }
    }

    suspend fun updateItem(item: Item) {
        if (item.id == 0) {
            _items.insert(item)
            Log.d("ControlPanelService", "added new item: ${item.id} ${item.label}")
        } else {
            _items.update(item)
            Log.d("ControlPanelService", "item updated: ${item.id} ${item.label}")
        }
    }
}