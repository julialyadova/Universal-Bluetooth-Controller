package com.example.ubc.ui.items.displays

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemSimpleDisplayBinding
import com.example.ubc.ui.items.DisplayView

class SimpleDisplayView @JvmOverloads constructor(
        item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : DisplayView(item, context, attrs, defStyleAttr) {

    private val binding = ItemSimpleDisplayBinding.inflate(LayoutInflater.from(context),this,true)

    init{
        binding.label.text = item.label
    }

    override fun receive(data: ByteArray) {
        binding.display.text = data.toString()
    }
}