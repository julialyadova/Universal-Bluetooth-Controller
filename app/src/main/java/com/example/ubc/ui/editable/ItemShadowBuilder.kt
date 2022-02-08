package com.example.ubc.ui.editable

import android.graphics.Canvas
import android.graphics.Point
import android.view.View

class ItemShadowBuilder (view: View) : View.DragShadowBuilder(view) {

    //private val shadow = ResourcesCompat.getDrawable(view.context.resources, R.drawable, view.context.theme)

    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        val width: Int = view.width

        val height: Int = view.height

        //shadow?.setBounds(0, 0, width, height)

        size.set(width, height)

        touch.set(width / 2, height / 2)
    }

    // 8
    override fun onDrawShadow(canvas: Canvas) {
        // 9
        view.draw(canvas)
       //shadow?.draw(canvas)
    }
}