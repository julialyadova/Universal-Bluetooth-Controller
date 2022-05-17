package com.example.ubc.ui.panel.items

import android.content.Context
import android.util.Log
import com.example.ubc.items.Item
import com.example.ubc.items.smf.ItemButton
import com.example.ubc.items.smf.ItemDisplay
import com.example.ubc.items.smf.ItemHistory
import com.example.ubc.items.smf.ItemSwitch
import com.example.ubc.ui.panel.items.ubcarduino.ButtonView
import com.example.ubc.ui.panel.items.ubcarduino.HistoryView
import com.example.ubc.ui.panel.items.ubcarduino.SimpleDisplayView
import com.example.ubc.ui.panel.items.ubcarduino.SwitchView

class ItemViewFactory(val context: Context) {

    fun create(item: Item): ItemView? {
        log("create(${item.id} ${item.label})")

        val itemView = when(item) {
            is ItemButton -> ButtonView(item, context)
            is ItemHistory -> HistoryView(item, context)
            is ItemSwitch -> SwitchView(item, context)
            is ItemDisplay -> SimpleDisplayView(item,context)
            else -> null
        }

        if (itemView == null) {
            Log.e("ItemViewFactory","Can't assign any ItemView class to item of class ${item::class}")
        }

        return itemView
    }

    private fun log(message: String) {
        Log.d("ItemViewFactory", message)
    }
}