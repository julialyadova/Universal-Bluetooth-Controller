package com.example.ubc.ui.items.ubcarduino

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import com.example.ubc.databinding.ItemJoystickBinding
import com.example.ubc.items.smf.ItemJoystick
import com.example.ubc.ui.items.ItemView

class JoystickView @JvmOverloads constructor(
    private val item: ItemJoystick,
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemJoystickBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.joystick.isAutoReCenterButton = item.centering
        binding.joystick.setOnMoveListener( { angle, strength ->
            item.move(angle, strength)
            Log.d("Joystick", "move raw (angle: $angle, strength: $strength)")
            send(item.getData())
        }, item.sendRate)
    }

    override fun onDataReceived(data: ByteArray) {}
}