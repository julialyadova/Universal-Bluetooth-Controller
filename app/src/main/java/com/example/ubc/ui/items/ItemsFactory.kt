package com.example.ubc.ui.items

import android.content.Context
import android.util.Log
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.items.displays.HistoryView
import com.example.ubc.ui.items.displays.SimpleDisplayView
import com.example.ubc.ui.items.senders.ButtonView
import com.example.ubc.ui.items.senders.SwitchView

class ItemsFactory(val context: Context, val dataSender: DataSender) {

    fun create(item: Item): ItemView? {
        Log.d("ItemsFactory", "creating item view of type ${item.type}")
        return when(item.type) {
            Item.Types.BUTTON -> ButtonView(item, context, dataSender)
            Item.Types.SWITCH -> SwitchView(item, context, dataSender)
            Item.Types.SIMPLE_DISPLAY -> SimpleDisplayView(item, context)
            Item.Types.HISTORY -> HistoryView(item, context)
            else -> null
        }
    }
}