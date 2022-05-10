package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemControlBinding
import com.example.ubc.ui.panel.items.ItemView

class ButtonView @JvmOverloads constructor(
    private val item: Item,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemControlBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.controlText.text = item.label
        binding.controlText.setOnClickListener {
            send("button clicked".toByteArray())
        }
    }

    override fun onDataReceived(data: ByteArray) { }
}