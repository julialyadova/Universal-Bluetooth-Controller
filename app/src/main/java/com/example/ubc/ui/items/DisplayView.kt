package com.example.ubc.ui.items

import android.content.Context
import android.util.AttributeSet
import com.example.ubc.data.entities.Item

abstract class DisplayView @JvmOverloads constructor(
        item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ItemView(item,context, attrs, defStyleAttr) {

    abstract fun receive(data: ByteArray)
}