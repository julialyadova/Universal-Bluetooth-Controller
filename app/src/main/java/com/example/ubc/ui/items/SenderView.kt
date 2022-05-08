package com.example.ubc.ui.items

import android.content.Context
import android.util.AttributeSet
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.main.viewmodels.ItemViewModel

abstract class SenderView @JvmOverloads constructor(
        item: Item,
        context: Context,
        private val viewModel: ItemViewModel,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ItemView(item,context, attrs, defStyleAttr) {

    protected fun send(data: ByteArray) {
        viewModel.send(data)
    }
}