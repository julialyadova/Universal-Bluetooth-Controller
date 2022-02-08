package com.example.ubc.ui.items

import android.content.Context
import android.util.Log
import com.example.ubc.data.entities.Item

class ItemsFactory(val context: Context) {

    fun create(item: Item): ItemView? {
        Log.d("ItemsFactory", "creating item view of type ${item.type}")
        return when(item.type) {
            ItemTypes.BUTTON -> ButtonView(item, context)
            ItemTypes.SWITCH -> SwitchView(item, context)
            else -> null
        }
    }
}

class ItemTypes{
    companion object {
        val BUTTON = "button"
        val SWITCH = "switch"
    }
}