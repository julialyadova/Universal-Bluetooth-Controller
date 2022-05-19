package com.example.ubc.ui.panel.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemSliderBinding
import com.example.ubc.items.smf.ItemScale
import com.example.ubc.ui.panel.items.ItemView

class ScaleView @JvmOverloads constructor(
    private val item: ItemScale,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemSliderBinding.inflate(LayoutInflater.from(context),this,true)

    init {

    }

    override fun onDataReceived(data: ByteArray) { }
}