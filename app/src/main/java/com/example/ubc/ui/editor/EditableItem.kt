package com.example.ubc.ui.editor

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.ui.items.ItemView
import com.example.ubc.ui.items.ItemViewFactory
import com.example.ubc.ui.shared.Graphics


class EditableItem constructor(
    val item: Item,
    @LayoutRes private val recourse: Int,
    context: Context
) : ConstraintLayout(context, null, 0) {

    val view = this
    val itemId = item.id
    private lateinit var _shadowBuilder : DragShadowBuilder
    private var _onClickListener : ((item: Item) -> Unit)? = null
    private lateinit var _itemView : ItemView

    fun setOnClickListener(l: ((item: Item) -> Unit)?) {
        _onClickListener = l
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        addItemView()
        addHandler()

        Graphics.setSize(this, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setPosition(item.x.toInt(), item.y.toInt())
        _shadowBuilder = ViewShadowBuilder(this)
    }

    private fun addItemView() {
        _itemView = ItemViewFactory(context).create(item)
        _itemView.allViews.forEach { it.isEnabled = false }
        addView(_itemView)
    }

    private fun addHandler() {
        val handler = LayoutInflater.from(context).inflate(R.layout.editor_handle, this, false)
        handler.setOnClickListener {
            _onClickListener?.invoke(item)
        }
        handler.setOnLongClickListener {
            drag()
            true
        }
        addView(handler)
    }

    private fun drag() {
        Log.d("", "drag started")
        val item = ClipData.Item(item.id.toString())
        val dataToDrag = ClipData(
            item.text,
            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
            item
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            @Suppress("DEPRECATION")
            this.startDrag(dataToDrag, _shadowBuilder, this, 0)//support pre-Nougat versions
        } else {
            this.startDragAndDrop(dataToDrag, _shadowBuilder, this, 0)
        }

        this.visibility = View.INVISIBLE
    }

    fun drop(x: Int, y: Int) {
        setPosition(x, y)
        Log.d("ItemView", "item dropped at ($x; $y) ${layoutParams.width}")
        this.visibility = View.VISIBLE
    }

    private fun setPosition(x: Int, y: Int) {
        val params = this.layoutParams as MarginLayoutParams
        params.leftMargin = x
        params.topMargin = y
        this.layoutParams = params
    }

    fun cancelDrag() {
        this.visibility = View.VISIBLE
    }
}