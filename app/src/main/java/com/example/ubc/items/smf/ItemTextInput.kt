package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemTextInput : Item() {
    var command: String = "on"
    var maxLength = 100
    var text : String = ""
        set(value) {field = text.take(maxLength)}

    fun getData() : ByteArray{
        return SMFBuilder().putCommand(command).putString(text).build()
    }

    override fun getParams() : List<ItemParam> = listOf(
        ItemParam.text("Команда", command) { value -> command = value})

    override fun getParamValues(): List<KeyValuePair> = listOf(
        KeyValuePair("command", command))

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command" -> command = param.value
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_text_input
}