package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemSimpleDisplayBinding
import com.example.ubc.items.smf.ItemDisplay
import com.example.ubc.ui.panel.items.ItemView

class SimpleDisplayView @JvmOverloads constructor(
    item: ItemDisplay,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemSimpleDisplayBinding.inflate(LayoutInflater.from(context),this,true)

    init{
        binding.label.text = item.label
    }

    override fun onDataReceived(data: ByteArray) {
        binding.display.text = String(data)
    }
}