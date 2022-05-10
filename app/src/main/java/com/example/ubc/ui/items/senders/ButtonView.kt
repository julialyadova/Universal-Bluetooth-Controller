package com.example.ubc.ui.items.senders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemControlBinding
import com.example.ubc.ui.items.SenderView
import com.example.ubc.ui.main.viewmodels.ItemViewModel

class ButtonView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        private val viewModel: ItemViewModel,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = item.id
) : SenderView(item, context, viewModel, attrs, defStyleAttr) {

    private val binding = ItemControlBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.controlText.text = item.label
        binding.controlText.setOnClickListener {
            send("button clicked".toByteArray())
        }
    }
}