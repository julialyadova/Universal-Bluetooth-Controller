package com.example.ubc.ui.items

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.ubc.data.entities.Item

abstract class ItemView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    abstract fun getDragHandler() : View
    abstract fun getShadowBuilder() : DragShadowBuilder
    abstract fun getCreateDialog() : DialogFragment
    abstract fun getEditDialog() : DialogFragment
    fun getItemId() : Int = item.id

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPosition(item.x.toInt(), item.y.toInt())
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
            this.startDrag(dataToDrag, getShadowBuilder(), this, 0)//support pre-Nougat versions
        } else {
            this.startDragAndDrop(dataToDrag, getShadowBuilder(), this, 0)
        }

        this.alpha = 0.1f
    }

    fun drop(x: Int, y: Int) {
        setPosition(x,y)
        Log.d("ItemView", "item dropped at ($x; $y) ${layoutParams.width}")
        this.alpha = 1f
    }

    private fun setPosition(x: Int, y: Int) {
        val param = this.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(x,y,0,0)
        this.layoutParams = param
    }

    fun cancelDrag() {
        Log.d("ItemView", "drag operation finished unsuccessfully. Item was returned to the start position")
        this.alpha = 1f
    }
}