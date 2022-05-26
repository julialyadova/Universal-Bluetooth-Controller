package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemSlider : Item() {
    override var label = "слайдер"
    var command: String = "slider"
    var min: Int = 0
    var max: Int = 255
    var step: Int = 1
    var default: Int = 128
    var value: Int = default

    fun getData() : ByteArray {
        return SMFBuilder().putCommand(command).putInt(value).build()
    }

    fun setSliderValue(v: Int) {
        value = v.coerceIn(min, max)
    }

    fun getCommandWithValue() : String {
        return "$command\$$value"
    }

    override fun getParams() : List<ItemParam> = listOf(
        ItemParam.text("Команда", command) { value -> command = value},
        ItemParam.integer("MIN", min, 0, 254) { value -> min = value},
        ItemParam.integer("MAX", max, 1, 255) { value -> max = value.coerceAtLeast(min + 1) },
        ItemParam.integer("шаг", step, 1, 255) { value -> step = value},
        ItemParam.integer("Значение по умолчанию", default, 0, 255) { value -> default = value.coerceAtMost(max)}
    )

    override fun getParamValues(): List<KeyValuePair> = listOf(
            KeyValuePair("command", command),
            KeyValuePair("min", min.toString()),
            KeyValuePair("max", max.toString()),
            KeyValuePair("step", step.toString()),
            KeyValuePair("default", default.toString()),
    )

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command" -> command = param.value
                "min" -> min = param.value.toInt()
                "max" -> max = param.value.toInt()
                "step" -> step = param.value.toInt()
                "default" -> default = param.value.toInt()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_slider
}