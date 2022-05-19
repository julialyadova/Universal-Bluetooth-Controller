package com.example.ubc.data

import com.beust.klaxon.Klaxon
import com.example.ubc.data.entities.ItemEntity
import com.example.ubc.items.Item
import com.example.ubc.items.ItemDefinition
import com.example.ubc.items.KeyValuePair

class ControlPanelFactory {
    fun createItem(entity: ItemEntity) : Item {
        val item : Item? = ItemDefinition.definitions[entity.type]?.itemBinding?.invoke()

        if (item == null) {
            throw Exception("item with type ${entity.type} was not assigned to Item class")
        }

        item.id = entity.id
        item.type = entity.type
        item.panelId = entity.panelId
        item.label = entity.label
        item.x = entity.x
        item.y = entity.y
        val params = Klaxon().parseArray<KeyValuePair>(entity.data)
        if (params != null)
            item.setParams(params)
        return item
    }
}