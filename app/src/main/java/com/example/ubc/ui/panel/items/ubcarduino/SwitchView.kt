package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemSwitchBinding
import com.example.ubc.ui.panel.items.ItemView

class SwitchView @JvmOverloads constructor(
    private val item: Item,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemSwitchBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.itemSwitchLabel.text = item.label
        binding.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            send("switch ${item.label} state: $isChecked".toByteArray())
        }
    }

    override fun onDataReceived(data: ByteArray) { }
}