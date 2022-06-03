package com.example.ubc.ui.items

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.ubc.items.Item

abstract class ItemView @JvmOverloads constructor(
    private val item: Item,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var dataSendActionListener: ((data: ByteArray) -> Unit)? = null

    fun setOnDataSendActionListener(l: (data: ByteArray) -> Unit) {
        dataSendActionListener = l
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setPosition(item.x.toInt(), item.y.toInt())
    }

    private fun setPosition(x: Int, y: Int) {
        val param = this.layoutParams as ViewGroup.MarginLayoutParams
        param.setMargins(x,y,0,0)
        this.layoutParams = param
    }

    protected fun send(data: ByteArray) {
        dataSendActionListener?.invoke(data)
    }

    abstract fun onDataReceived(data: ByteArray)
}