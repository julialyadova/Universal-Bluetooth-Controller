package com.example.ubc.ui.editor

import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.items.EditorItem
import com.example.ubc.ui.main.fragments.EditorFragment
import kotlin.math.roundToInt


class EditableCanvas(
        private val canvasLayout: ViewGroup,
        private val editorFragment: EditorFragment
) {
    private val _gridSize = 50
    private val factory = EditorItemFactory(canvasLayout.context, canvasLayout, editorFragment._viewModel)
    private var _items : MutableList<EditorItem> = mutableListOf()

    init {
        setCanvasDragEvents(canvasLayout)
    }

    fun update(items: List<Item>) {
        canvasLayout.removeAllViews()
        for (item in items) {
            val item = factory.create(item)
            _items.add(item)
        }
    }

    private fun getDroppedItem(e: DragEvent) : EditorItem? {
        val id = Integer.parseInt(e.clipData.getItemAt(0).text.toString())
        return _items.firstOrNull { i -> i.itemId == id }
    }

    private fun setCanvasDragEvents(canvas: View) {
        canvas.setOnDragListener { view, e ->
            when (e.action) {
                DragEvent.ACTION_DROP -> {
                    val item = getDroppedItem(e)!!
                    val x = (e.x / _gridSize).roundToInt() * _gridSize - item.view.width / 2
                    val y = (e.y / _gridSize).roundToInt() * _gridSize - item.view.height / 2
                    item.drop(x, y)
                    Log.d("Drag And Drop", "item with id ${item.itemId} was dropped at (${e.x};${e.y})")
                    editorFragment._viewModel.setPosition(item.itemId, x, y)
                    true
                }
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_ENDED -> true
                DragEvent.ACTION_DRAG_ENTERED -> true //ignore event
                DragEvent.ACTION_DRAG_LOCATION -> true //ignore event
                DragEvent.ACTION_DRAG_EXITED -> true //ignore event
                else -> false
            }
            true
        }
    }

    private fun setItemDragEvents(item: View) {
        item.setOnDragListener { view, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.alpha = 0.8f
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item = getDroppedItem(e)
                    Log.d("Drag And Drop", "item with id ${item?.itemId} was dropped at the other item")
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.alpha = 1f
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> true //ignore event
                else -> false
            }
            true
        }
    }

    private fun setTrashBinDragEvents(canvas: View) {
        canvas.setOnDragListener { view, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    //todo: show grid
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    //get data from dropped object
                    val item = getDroppedItem(e)
                    Log.d("Drag And Drop", "item with id ${item?.itemId} was dropped at (${e.x};${e.y})")
                    //todo: find dropped item by id and call drop(x,y)
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    //todo: hide grid
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> true //ignore event
                DragEvent.ACTION_DRAG_LOCATION -> true //ignore event
                DragEvent.ACTION_DRAG_EXITED -> true //ignore event
                else -> false
            }
            true
        }
    }
}