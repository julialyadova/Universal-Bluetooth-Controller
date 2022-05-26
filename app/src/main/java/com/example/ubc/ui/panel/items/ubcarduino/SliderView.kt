package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import com.example.ubc.databinding.ItemSliderBinding
import com.example.ubc.items.smf.ItemSlider
import com.example.ubc.ui.panel.items.ItemView

class SliderView @JvmOverloads constructor(
    private val item: ItemSlider,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr),
    SeekBar.OnSeekBarChangeListener {

    private val binding = ItemSliderBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        binding.itemSliderLabel.text = item.label
        val range = item.max - item.min
        var steps = range / item.step
        binding.itemSliderBar.max = steps
        binding.itemSliderBar.progress = item.default / item.step
        binding.itemSliderValue.visibility = View.INVISIBLE
        binding.itemSliderBar.setOnSeekBarChangeListener(this)
    }

    override fun onDataReceived(data: ByteArray) { }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        item.setSliderValue(progress * item.step + item.min)
        binding.itemSliderValue.text = item.value.toString()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        binding.itemSliderLabel.visibility = View.INVISIBLE
        binding.itemSliderValue.visibility = View.VISIBLE
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        send(item.getData())
        binding.itemSliderLabel.visibility = View.VISIBLE
        binding.itemSliderValue.visibility = View.INVISIBLE
    }
}