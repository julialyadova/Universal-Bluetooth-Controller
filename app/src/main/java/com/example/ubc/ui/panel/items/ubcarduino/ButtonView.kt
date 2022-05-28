package com.example.ubc.ui.panel.items.ubcarduino

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemButtonBinding
import com.example.ubc.items.params.ColorParam
import com.example.ubc.items.smf.ItemButton
import com.example.ubc.ui.panel.items.ItemView
import com.example.ubc.ui.shared.Graphics

@SuppressLint("ClickableViewAccessibility")
class ButtonView @JvmOverloads constructor(
    private val item: ItemButton,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemButtonBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.itemButtonText.text = item.label
        Graphics.setBackgroundColor(binding.itemButtonLayout, item.color)

        Graphics.addPressAnimationOnTouch(binding.itemButtonText, binding.itemButtonLayout)

        binding.itemButtonText.setOnClickListener {
            send(item.getData())
        }
    }

    override fun onDataReceived(data: ByteArray) { }
}