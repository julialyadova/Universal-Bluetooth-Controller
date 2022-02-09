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

    fun delete() {
        //todo: delete item from db
    }

    fun drag() {
        Log.d("ItemView", "drag started")
        val item = ClipData.Item(item.id.toString())
        val dataToDrag = ClipData(
                item.text,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
        )

        //start drag and drop operation
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            @Suppress("DEPRECATION")
            this.startDrag(dataToDrag, getShadowBuilder(), this, 0)//support pre-Nougat versions
        } else {
            //supports Nougat and beyond
            this.startDragAndDrop(dataToDrag, getShadowBuilder(), this, 0)
        }


        //hide view while dragging
        this.alpha = 0.1f
    }

    fun drop(x: Int, y: Int) {
        //set new margins
        //todo: update position in stored item
        val param = this.layoutParams as ViewGroup.MarginLayoutParams
        val offsetX = getShadowBuilder().view.width / 2
        val offsetY = getShadowBuilder().view.height / 2
        param.setMargins(x - offsetX,y - offsetY,0,0)
        this.layoutParams = param
        Log.d("ItemView", "item dropped at ($x; $y) ${layoutParams.width}")
        this.alpha = 1f
    }

    fun cancelDrag() {
        Log.d("ItemView", "drag operation finished unsuccessfully. Item was returned to the start position")
        this.alpha = 1f
    }
}