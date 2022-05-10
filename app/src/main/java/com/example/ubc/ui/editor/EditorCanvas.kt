package com.example.ubc.ui.editor

import android.view.DragEvent
import android.view.ViewGroup
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.items.EditableItem
import kotlin.math.roundToInt

class EditorCanvas(
    private val canvasLayout: ViewGroup
) {
    private val _gridSize = 50
    private val _factory = EditorItemFactory(canvasLayout.context, canvasLayout)
    private var _items : MutableList<EditableItem> = mutableListOf()
    private var _itemMovedListener: ((itemId: Int, x: Int, y: Int) -> Unit)? = null
    private var _itemClickedListener: ((item: Item) -> Unit)? = null

    init {
        canvasLayout.setOnDragListener { _, e ->
            when (e.action) {
                DragEvent.ACTION_DROP -> {
                    val item = getItemFromClipData(e)
                    dropItem(item, e.x, e.y)
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if (!e.result) {
                        resetDraggedItem()
                    }
                }
            }
            true
        }
    }

    fun setOnItemMovedListener(l: (itemId: Int, x: Int, y: Int) -> Unit) {
        _itemMovedListener = l
    }

    fun setOnItemClickListener(l: (item: Item) -> Unit) {
        _itemClickedListener = l
    }

    fun setItems(items: List<Item>) {
        canvasLayout.removeAllViews()
        for (item in items) {
            val editableItem = _factory.create(item)
            editableItem.setOnClickListener(_itemClickedListener)
            _items.add(editableItem)
        }
    }

    private fun getItemFromClipData(e: DragEvent) : EditableItem {
        val id = Integer.parseInt(e.clipData.getItemAt(0).text.toString())
        return _items.first { i -> i.itemId == id }
    }

    private fun dropItem(item: EditableItem, x: Float, y: Float) {
        val realX = (x / _gridSize).roundToInt() * _gridSize - item.view.width / 2
        val realY = (y / _gridSize).roundToInt() * _gridSize - item.view.height / 2
        item.drop(realX, realY)
        _itemMovedListener?.invoke(item.itemId,realX,realY)
    }

    private fun resetDraggedItem() {
        for (item in _items) {
            item.cancelDrag()
        }


    }
}