package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.R
import com.example.ubc.databinding.ItemControlBinding
import com.example.ubc.items.smf.ItemButton
import com.example.ubc.ui.panel.items.ItemView

class ButtonView @JvmOverloads constructor(
    private val item: ItemButton,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemControlBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.controlText.text = item.label
        val color = when(item.color) {
            ItemButton.COLOR_GREEN -> R.color.green
            ItemButton.COLOR_RED -> R.color.red
            ItemButton.COLOR_YELLOW -> R.color.yellow
            else -> R.color.blue
        }
        binding.controlText.setBackgroundTintList(ColorStateList.valueOf(resources.getColor(color)))
        binding.controlText.setOnClickListener {
            send(item.command.toByteArray())
        }
    }

    override fun onDataReceived(data: ByteArray) { }
}