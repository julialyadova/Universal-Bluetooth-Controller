package com.example.ubc.ui.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.R
import com.example.ubc.databinding.ItemIndicatorBinding
import com.example.ubc.items.smf.ItemIndicator
import com.example.ubc.ui.items.ItemView

class IndicatorView @JvmOverloads constructor(
    private val item: ItemIndicator,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemIndicatorBinding.inflate(LayoutInflater.from(context),this,true)

    init{
        binding.itemIndicatorLabel.text = item.label
    }

    override fun onDataReceived(data: ByteArray) {
        item.receiveData(data)
        if (item.isOn)
            binding.itemIndicatorImg.setImageResource(R.drawable.ic_indicator_on)
        else
            binding.itemIndicatorImg.setImageResource(R.drawable.ic_indicator_off)
    }
}