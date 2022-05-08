package com.example.ubc.ui.items

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
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.editor.ViewShadowBuilder

class EditorItem @JvmOverloads constructor(
        private val item: Item,
        @LayoutRes private val resourse: Int,
        context: Context
) : ConstraintLayout(context, null, 0) {

    val view = this
    val itemId = item.id
    private val shadowBuilder = ViewShadowBuilder(this)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        LayoutInflater.from(context).inflate(resourse, this, true)
        setWrapContentSize()
        bindItem()
        setPosition(item.x.toInt(), item.y.toInt())
        for (v in allViews) {
            v.setOnLongClickListener {
                drag()
                true
            }
        }
    }

    fun bindItem() {
        this.findViewWithTag<TextView>("label")?.text = item.label
    }

    fun setWrapContentSize() {
        val params = this.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        this.layoutParams = params
    }

    fun drag() {
        Log.d("ItemView", "drag started")
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
        val params = this.layoutParams as ViewGroup.MarginLayoutParams
        params.leftMargin = x
        params.topMargin = y
        this.layoutParams = params
    }

    fun cancelDrag() {
        Log.d("ItemView", "drag operation finished unsuccessfully. Item was returned to the start position")
        this.alpha = 1f
    }
}