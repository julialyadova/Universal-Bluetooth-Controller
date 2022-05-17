package com.example.ubc.ui.editor

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import com.example.ubc.items.Item

class EditorItemFactory (
        private val context: Context,
        private val root: ViewGroup,
) {
    fun create(item: Item): EditableItem {
        Log.d("EditorItemFactory", "create(${item.id} ${item.label})")

        val resource = item.getLayoutRes()
        val editorItem = EditableItem(item, resource, context)
        root.addView(editorItem)
        return editorItem
    }
}