package com.example.ubc.ui.panel.items.ubcarduino

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemIconButtonBinding
import com.example.ubc.items.params.IconParam
import com.example.ubc.items.smf.ItemIconButton
import com.example.ubc.ui.panel.items.ItemView
import com.example.ubc.ui.shared.Graphics

@SuppressLint("ClickableViewAccessibility")
class IconButtonView @JvmOverloads constructor(
    private val item: ItemIconButton,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemIconButtonBinding.inflate(LayoutInflater.from(context),this,true)

    init {
        val res = IconParam.icons[item.icon]
        if (res != null)
            binding.itemIconButtonImg.setImageResource(res)

        Graphics.addPressAnimationOnTouch(binding.itemIconButtonImg)

        binding.itemIconButtonImg.setOnClickListener {
            send(item.getData())
        }
    }

    override fun onDataReceived(data: ByteArray) { }
}