package com.example.ubc.items.params

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat
import com.example.ubc.R
import com.example.ubc.databinding.LayoutHorizontalScrollBinding
import com.example.ubc.items.ItemParam
import com.example.ubc.ui.shared.Graphics

class ColorParam (
    name: String,
    var value: Int,
    var setter: (Int) -> Unit
) : ItemParam(name) {

    private var newValue = value

    override fun createView(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val scrollBinding = LayoutHorizontalScrollBinding.inflate(inflater)
        val buttons = mutableListOf<ImageButton>()

        for (color in colors) {
            val colorInt = ResourcesCompat.getColor(context.resources, color.value, null)

            val button = ImageButton(context)
            buttons.add(button)
            button.setBackgroundResource(R.drawable.ic_circle)
            Graphics.setBackgroundColor(button, colorInt)
            if (colorInt == value)
                button.setImageResource(R.drawable.ic_check)

            button.setOnClickListener {
                newValue = colorInt
                buttons.forEach { b -> b.setImageDrawable(null) }
                button.setImageResource(R.drawable.ic_check)
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

    companion object {
        val colors = mapOf(
            "default" to R.color.item_default,
            "yellow" to R.color.yellow,
            "green" to R.color.green,
            "red" to R.color.red,
            "blue" to R.color.blue,
            "grey" to R.color.grey
        )
    }
}