package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair

class ItemDisplay : Item() {
    var command: String = "display"

    override fun getParams() : List<ItemParam> = listOf(
        ItemParam.text("Идентификатор", command) { value -> command = value.replace(' ','_')})

    override fun getParamValues(): List<KeyValuePair> = listOf(
        KeyValuePair("command", command))

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command" -> command = param.value
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_simple_display
}