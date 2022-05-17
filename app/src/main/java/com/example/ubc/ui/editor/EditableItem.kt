package com.example.ubc.ui.editor

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import com.example.ubc.items.Item

class EditableItem constructor(
    val item: Item,
    @LayoutRes private val recourse: Int,
    context: Context
) : ConstraintLayout(context, null, 0) {

    val view = this
    val itemId = item.id
    private val shadowBuilder = ViewShadowBuilder(this)
    private var onClickListener : ((item: Item) -> Unit)? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LayoutInflater.from(context).inflate(recourse, this, true)
        setSize()
        bindItem()
        setPosition(item.x.toInt(), item.y.toInt())
        for (v in allViews) {
            v.setOnLongClickListener {
                drag()
                true
            }
            v.setOnClickListener {
                onClickListener?.invoke(item)
            }
        }
    }

    fun setOnClickListener(l: ((item: Item) -> Unit)?) {
        onClickListener = l
    }

    private fun bindItem() {
        this.findViewWithTag<TextView>("label")?.text = item.label
    }

    private fun setSize() {
        val params = this.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.layoutParams = params
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
            this.startDrag(dataToDrag, shadowBuilder, this, 0)//support pre-Nougat versions
        } else {
            this.startDragAndDrop(dataToDrag, shadowBuilder, this, 0)
        }

        this.alpha = 0.1f
    }

    fun drop(x: Int, y: Int) {
        setPosition(x,y)
        Log.d("ItemView", "item dropped at ($x; $y) ${layoutParams.width}")
        this.alpha = 1f
    }

    private fun setPosition(x: Int, y: Int) {
        val params = this.layoutParams as MarginLayoutParams
        params.leftMargin = x
        params.topMargin = y
        this.layoutParams = params
    }

    fun cancelDrag() {
        this.alpha = 1f
    }
}