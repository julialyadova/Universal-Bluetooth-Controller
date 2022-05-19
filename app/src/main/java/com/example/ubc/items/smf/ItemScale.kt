package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair

class ItemScale : Item() {
    override var label = "шкала"
    var command: String = "scale"
    var min: Int = 0
    var max: Int = 255
    var value: Int = 0
        private set

    fun processCommand(message: String) {
        val params = message.split("$")
        if (params.size == 2 && params[0] == command) {
            value = params[1].toIntOrNull() ?: value
        }
    }

    override fun getParams() : List<ItemParam> = listOf(
        ItemParam.text("Команда", command) { value -> command = value},
        ItemParam.integer("MIN", min, 0, 254) { value -> min = value},
        ItemParam.integer("MAX", max, 1, 255) { value -> max = value.coerceAtLeast(min + 1) }
    )

    override fun getParamValues(): List<KeyValuePair> = listOf(
        KeyValuePair("command", command),
        KeyValuePair("min", min.toString()),
        KeyValuePair("max", max.toString())
    )

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command" -> command = param.value
                "min" -> min = param.value.toInt()
                "max" -> max = param.value.toInt()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_slider
}