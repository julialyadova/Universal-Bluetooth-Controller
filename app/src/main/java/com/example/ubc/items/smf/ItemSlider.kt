package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.IntParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemSlider : Item() {
    override var label = "слайдер"
    var command: String = "slider"
    var min: Int = 0
    var max: Int = 255
    var step: Int = 1
    var default: Int = 128
    var value: Int = default

    init {
        addStoredParam("command", {command}, {command = it})
        addStoredParam("min", {min.toString()}, {min = it.toInt()})
        addStoredParam("max", {max.toString()}, {max = it.toInt()})
        addStoredParam("step", {step.toString()}, {step = it.toInt()})
        addStoredParam("default", {default.toString()}, {default = it.toInt()})
    }

    fun getData() : ByteArray {
        return SMFBuilder().putCommand(command).putInt(value).build()
    }

    fun setSliderValue(v: Int) {
        value = v.coerceIn(min, max)
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
        StringParam("Команда", command,8 ) { command = it},
        IntParam("MIN", min, 0, 254) { min = it},
        IntParam("MAX", max, 1, 255) { max = it.coerceAtLeast(min + 1) },
        IntParam("шаг", step, 1, 255) { step = it},
        IntParam("Значение по умолчанию", default, 0, 255) { default = it.coerceAtMost(max)}
    )

    override fun getLayoutRes(): Int = R.layout.item_slider
}