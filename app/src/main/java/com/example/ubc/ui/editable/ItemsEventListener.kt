package com.example.ubc.ui.editable

import com.example.ubc.data.entities.Item

interface ItemsEventListener {
    fun onAddItem(item: Item)
    fun onMoveItem(item: Item)
    fun onRemoveItem(item: Item)
}