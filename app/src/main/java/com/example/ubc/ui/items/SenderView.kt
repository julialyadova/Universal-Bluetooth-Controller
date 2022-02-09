package com.example.ubc.ui.items

import android.content.Context
import android.util.AttributeSet
import com.example.ubc.data.entities.Item

abstract class SenderView @JvmOverloads constructor(
        item: Item,
        context: Context,
        private val dataSender: DataSender,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ItemView(item,context, attrs, defStyleAttr) {

    protected fun send(data: String) {
        dataSender.send(data)
    }
}

interface DataSender {
    fun send(data: String)
}