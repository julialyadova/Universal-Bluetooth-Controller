package com.example.ubc.data

import com.beust.klaxon.Klaxon
import com.example.ubc.data.entities.ItemEntity
import com.example.ubc.items.Item
import com.example.ubc.items.KeyValuePair
import com.example.ubc.items.smf.ItemButton
import com.example.ubc.items.smf.ItemDisplay
import com.example.ubc.items.smf.ItemHistory
import com.example.ubc.items.smf.ItemSwitch

class ControlPanelFactory {
    fun createItem(entity: ItemEntity) : Item {
        val item : Item = when(entity.type) {
            ItemEntity.Types.BUTTON -> ItemButton()
            ItemEntity.Types.SWITCH -> ItemSwitch()
            ItemEntity.Types.HISTORY -> ItemHistory()
            ItemEntity.Types.SIMPLE_DISPLAY -> ItemDisplay()
            else -> ItemButton()
        }
        item.id = entity.id
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