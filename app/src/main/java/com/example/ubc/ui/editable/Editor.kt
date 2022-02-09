package com.example.ubc.ui.editable

import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.FragmentManager
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.items.DisplayView
import com.example.ubc.ui.items.DataSender
import com.example.ubc.ui.items.ItemView
import com.example.ubc.ui.items.ItemsFactory
import kotlin.math.roundToInt


class Editor(
        private val fragmentManager: FragmentManager,
        private val canvasLayout: ViewGroup,
        private val dataSender: DataSender,
) {
    private val _gridSize = 50
    private val _items = listOf<ItemView>()
    private val _editable = true
    private val factory = ItemsFactory(canvasLayout.context, dataSender)

    init {
        setCanvasDragEvents(canvasLayout)
    }

    fun notifyItems(data: String) {
        for (view in canvasLayout.children) {
            if (view is DisplayView)
                view.recieve(data)
        }
    }

    fun update(items: List<Item>) {
        canvasLayout.removeAllViews()

        for (item in items) {
            val itemView = factory.create(item) ?: continue

            itemView.getDragHandler().setOnLongClickListener {
                if (_editable) {
                    Log.d("Editor", "Edit mode, item drag")
                    itemView.drag()
                }
                else {
                    Log.d("Editor", "Not edit mode, item edit")
                    itemView.getEditDialog().show(fragmentManager, "item edit dialog")
                }
                true
            }

            canvasLayout.addView(itemView)
        }
    }

    private fun findItemView(id: Int) : ItemView? {
        for (view in canvasLayout.children) {
            if (view is ItemView && view.getItemId() == id)
                return view
        }
        return null
    }

    private fun setCanvasDragEvents(canvas: View) {
        canvas.setOnDragListener { view, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    //todo: show grid
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    //get data from dropped object
                    val id = Integer.parseInt(e.clipData.getItemAt(0).text.toString())
                    Log.d("Drag And Drop", "item with id $id was dropped at (${e.x};${e.y})")
                    val x = (e.x / _gridSize).roundToInt() * _gridSize
                    val y = (e.y / _gridSize).roundToInt() * _gridSize
                    findItemView(id)?.drop(x,y)
                    //_viewModel.save()
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

    private fun setItemDragEvents(item: View) {
        item.setOnDragListener { view, e ->
            when (e.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    view.alpha = 0.8f
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    //get data from dropped object
                    val id = Integer.parseInt(e.clipData.getItemAt(0).text.toString())
                    Log.d("Drag And Drop", "item with id $id was dropped at the other item")
                    //todo: cancel drag operation for the dropped item
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    view.alpha = 1f
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    //todo: red tint on drag entered
                    view.invalidate()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    //todo: disable red tint on drag exit
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
                    val id = Integer.parseInt(e.clipData.getItemAt(0).text.toString())
                    Log.d("Drag And Drop", "item with id $id was dropped at (${e.x};${e.y})")
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