package com.example.ubc.ui.editor

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.example.ubc.R
import com.example.ubc.data.entities.Item

class EditorItemFactory (
        private val context: Context,
        private val root: ViewGroup,
) {
    fun create(item: Item): EditableItem {
        Log.d("EditorItemFactory", "creating editor item from item ${item.id}(${item.type})")

        val resource = when(item.type) {
            Item.Types.BUTTON -> R.layout.item_control
            Item.Types.SWITCH -> R.layout.item_switch
            Item.Types.SIMPLE_DISPLAY -> R.layout.item_simple_display
            Item.Types.HISTORY -> R.layout.item_history
            else -> 0
        }
        val editorItem = EditableItem(item, resource, context)
        root.addView(editorItem)
        return editorItem
    }
}