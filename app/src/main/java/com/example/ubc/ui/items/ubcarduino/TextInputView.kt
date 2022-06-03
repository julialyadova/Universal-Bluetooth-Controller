package com.example.ubc.ui.items.ubcarduino

import android.R.attr.maxLength
import android.content.Context
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemTextInputBinding
import com.example.ubc.items.smf.ItemTextInput
import com.example.ubc.ui.items.ItemView
import com.example.ubc.ui.shared.Graphics


class TextInputView @JvmOverloads constructor(
    private val item: ItemTextInput,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemTextInputBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.itemTextInputLabel.text = item.label
        binding.itemTextInput.filters = arrayOf(LengthFilter(maxLength))
        binding.itemTextInputButton.setOnClickListener {
            item.text = binding.itemTextInput.text.toString()
            send(item.getData())
        }
        Graphics.addPressAnimationOnTouch(binding.itemTextInputButton)
    }

    override fun onDataReceived(data: ByteArray) { }
}