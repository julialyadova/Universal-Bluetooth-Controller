package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemButton : Item() {
    var command: String = "btn"
    var color: String = COLOR_DEFAULT

    fun getData() : ByteArray {
        return SMFBuilder().putCommand(command).withNoArgs().build()
    }

    override fun getParams() : List<ItemParam> {
        val colors = listOf(COLOR_DEFAULT, COLOR_GREEN, COLOR_YELLOW, COLOR_RED)
        return listOf(
            ItemParam.text("Команда", command) { value -> command = value},
            ItemParam.category("Цвет", color, colors) { value -> color = value }
        )
    }

    override fun getParamValues(): List<KeyValuePair> {
        return listOf(
            KeyValuePair("command", command),
            KeyValuePair("color", color),
        )
    }

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command" -> command = param.value
                "color" -> color = param.value
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_button

    companion object {
        const val COLOR_DEFAULT : String = "Стандартный"
        const val COLOR_GREEN : String = "Зеленый"
        const val COLOR_RED : String = "Красный"
        const val COLOR_YELLOW : String = "Желтый"
    }
}