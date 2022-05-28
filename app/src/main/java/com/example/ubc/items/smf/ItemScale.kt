package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFReader

open class ItemScale : Item() {
    override var label = "шкала"
    var command: String = "scale"
    var min: Int = 0
    var max: Int = 255
    var value: Int = 0
        private set

    private val _reader = SMFReader()

    fun receiveData(data: ByteArray) {
        _reader.read(data).whenCommand(command).doIfIntArg {
            value = it.coerceIn(min, max)
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