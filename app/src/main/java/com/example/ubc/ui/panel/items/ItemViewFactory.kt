package com.example.ubc.ui.panel.items

import android.content.Context
import android.util.Log
import com.example.ubc.items.Item
import com.example.ubc.items.ItemDefinition

class ItemViewFactory(val context: Context) {

    fun create(item: Item): ItemView {
        log("create(${item.id} ${item.label})")

        val itemView = ItemDefinition.definitions[item.type]?.itemViewBinding?.invoke(item, context)

        if (itemView == null) {
            throw Exception("ItemViewFactory: Can't assign any ItemView class to item of class ${item::class}", )
        }

        return itemView
    }

    private fun log(message: String) {
        Log.d("ItemViewFactory", message)
    }
}