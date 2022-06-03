package com.example.ubc.ui.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemRoundScaleBinding
import com.example.ubc.items.smf.ItemRoundScale
import com.example.ubc.ui.items.ItemView

class RoundScaleView @JvmOverloads constructor(
    private val item: ItemRoundScale,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemRoundScaleBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.itemRoundScaleLabel.text = item.label
        val range = item.max - item.min
    }

    override fun onDataReceived(data: ByteArray) {
        item.receiveData(data)
        binding.itemRoundScale.setPercentage(100*(item.value - item.min)/(item.max - item.min))
        binding.itemRoundScaleValue.text = item.value.toString()
    }
}