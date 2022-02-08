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
import com.example.ubc.ui.editable.ItemShadowBuilder

abstract class ItemView @JvmOverloads constructor(
        item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    abstract fun getItemId() : Int
    abstract fun getDragHandler() : View
    abstract fun getCreateDialog() : DialogFragment
    abstract fun getEditDialog() : DialogFragment

    fun delete() {
        //todo: delete item from db
    }

    fun drag() {
        Log.d("ItemView", "drag started")
        val item = ClipData.Item(getItemId().toString())
        val dataToDrag = ClipData(
                item.text,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
        )

        val maskShadow = ItemShadowBuilder(getDragHandler())

        //start drag and drop operation
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            @Suppress("DEPRECATION")
            this.startDrag(dataToDrag, maskShadow, this, 0)//support pre-Nougat versions
        } else {
            //supports Nougat and beyond
            this.startDragAndDrop(dataToDrag, maskShadow, this, 0)
        }

        //hide view while dragging
        this.visibility = View.INVISIBLE
    }

    fun drop(x: Int, y: Int) {
        //set new margins
        //todo: update position in stored item
        val param = this.layoutParams as ViewGroup.MarginLayoutParams
        val offsetX = getDragHandler().width / 2
        val offsetY = getDragHandler().height / 2
        param.setMargins(x - offsetX,y - offsetY,0,0)
        this.layoutParams = param
        Log.d("ItemView", "item dropped at ($x; $y) ${layoutParams.width}")
        this.visibility = View.VISIBLE
    }

    fun cancelDrag() {
        Log.d("ItemView", "drag operation finished unsuccessfully. Item was returned to the start position")
        this.visibility = View.VISIBLE
    }
}