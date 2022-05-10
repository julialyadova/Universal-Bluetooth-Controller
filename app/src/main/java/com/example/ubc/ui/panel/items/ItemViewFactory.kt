package com.example.ubc.ui.panel.items

import android.content.Context
import android.util.Log
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.panel.items.ubcarduino.ButtonView
import com.example.ubc.ui.panel.items.ubcarduino.HistoryView
import com.example.ubc.ui.panel.items.ubcarduino.SimpleDisplayView
import com.example.ubc.ui.panel.items.ubcarduino.SwitchView

class ItemViewFactory(val context: Context) {

    fun create(item: Item): ItemView? {
        Log.d("ItemsFactory", "creating item view of type ${item.type}(${item.id}) ${item.x} ${item.y}")
        val itemView = when(item.type) {
            Item.Types.BUTTON -> ButtonView(item, context)
            Item.Types.SWITCH -> SwitchView(item, context)
            Item.Types.SIMPLE_DISPLAY -> SimpleDisplayView(item, context)
            Item.Types.HISTORY -> HistoryView(item, context)
            else -> null
        }

        if (itemView == null) {
            Log.e("ItemViewFactory", "Can't assign any ItemView class to item of type ${item.type}")
        }

        return itemView
    }
}