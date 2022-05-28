package com.example.ubc.items.params

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.ubc.R
import com.example.ubc.databinding.LayoutHorizontalScrollBinding
import com.example.ubc.items.ItemParam

class IconParam (
    name: String,
    var value: String,
    var setter: (String) -> Unit
) : ItemParam(name) {
    private var newValue = value

    private val icons = mapOf(
        "sync" to android.R.drawable.stat_notify_sync,
        "warning" to android.R.drawable.stat_sys_warning
    )

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun createView(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val scrollBinding = LayoutHorizontalScrollBinding.inflate(inflater)
        val buttons = mutableListOf<ImageButton>()

        for (icon in icons) {
            val button = ImageButton(context)
            buttons.add(button)

            button.setImageResource(icon.value)
            if (icon.key == value)
                button.setBackgroundResource(R.drawable.ic_status_connected)
            else
                button.setBackgroundResource(R.drawable.ic_status_disconnected)

            button.setOnClickListener {
                newValue = icon.key
                buttons.forEach { b -> b.setBackgroundResource(R.drawable.ic_status_disconnected) }
                button.setBackgroundResource(R.drawable.ic_status_connected)
            }

            scrollBinding.horizontalScrollContainer.addView(button)
            val params = button.layoutParams as ViewGroup.MarginLayoutParams
            params.height = 160
            params.width = 160
            params.setMargins(8,8,8,8)
            button.layoutParams = params
        }
        return scrollBinding.root
    }

    override fun submit() {
        setter(newValue)
    }
}