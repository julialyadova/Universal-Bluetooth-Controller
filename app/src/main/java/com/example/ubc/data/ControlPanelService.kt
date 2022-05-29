package com.example.ubc.data

import android.util.Log
import com.beust.klaxon.Klaxon
import com.example.ubc.data.entities.ItemEntity
import com.example.ubc.data.entities.PanelEntity
import com.example.ubc.data.repositories.ItemsRepository
import com.example.ubc.data.repositories.PanelsRepository
import com.example.ubc.items.Item
import com.example.ubc.items.Panel
import javax.inject.Inject

class ControlPanelService @Inject constructor(
        private val _items : ItemsRepository,
        private val _panels: PanelsRepository
) {
    private val _factory = ControlPanelFactory()

    suspend fun getAllPanels(): List<Panel> {
        return  _panels.getAll().map{Panel(it.id, it.name)}
    }

    suspend fun getPanelById(id: Int) : Panel {
        return fromEntity(_panels.getById(id))
    }

    suspend fun panelExists(id: Int) : Boolean {
        return _panels.exists(id)
    }

    suspend fun createPanel(name: String): Int {
        return _panels.add(name)
    }

    suspend fun deletePanel(id: Int) {
        _panels.delete(id)
    }

    suspend fun getItemsOfPanelWithId(id: Int) : List<Item> {
        return  _items.findByPanelId(id).map { i -> fromEntity(i) }
    }

    suspend fun renamePanel(id: Int, name: String) {
        val panel = _panels.getById(id)
        panel?.let {
            panel.name = name
            _panels.update(panel)

            log("panel $id was renamed")
        }
    }

    suspend fun addItemToPanel(type: String, panelId: Int) : Item {
        val item = ItemEntity(0, panelId, "label", type)
        _items.insert(item)

        log("item of type $type was added to panel with id $panelId. Added item id = ${item.id}")
        return fromEntity(item)
    }

    suspend fun deleteItem(id: Int) {
        val item = _items.getById(id)
        if (item != null) {
            _items.delete(item)
            log("item with id $id was deleted")
        }
    }

    suspend fun updateItem(item: Item) {
        val entity = _items.getById(item.id)
        if (entity == null) {
            log("item was not updated: item doesn't exist")
        } else {
            entity.x = item.x
            entity.y = item.y
            entity.label = item.label
            entity.data = Klaxon().toJsonString(item.getParamValues())
            _items.update(entity)
        }
    }

    private fun fromEntity(entity: PanelEntity) : Panel {
        return Panel(entity.id, entity.name)
    }

    private fun fromEntity(entity: ItemEntity) : Item {
        return _factory.createItem(entity)
    }

    private fun log(message: String) {
        Log.d("ControlPanelService", message)
    }
}