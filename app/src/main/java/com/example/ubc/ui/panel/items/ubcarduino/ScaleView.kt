package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.example.ubc.databinding.ItemScaleBinding
import com.example.ubc.items.smf.ItemScale
import com.example.ubc.ui.panel.items.ItemView

class ScaleView @JvmOverloads constructor(
    private val item: ItemScale,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemScaleBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.itemScaleLabel.text = item.label
        val range = item.max - item.min
        binding.itemScaleBar.max = range
        binding.itemScaleValue.visibility = if (item.showValue) View.VISIBLE else View.GONE
    }

    override fun onDataReceived(data: ByteArray) {
        item.receiveData(data)
        binding.itemScaleBar.progress = item.value - item.min
        binding.itemScaleValue.text = item.value.toString()
    }
}