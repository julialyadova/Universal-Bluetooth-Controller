package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemTextInput : Item() {
    var command: String = "on"
    val maxLength = 100
    var text : String = ""
        set(value) {field = value.take(maxLength)}

    init {
        addStoredParam("command", { command }, {command = it})
    }

    fun getData() : ByteArray{
        return SMFBuilder().putCommand(command).putString(text).build()
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
        StringParam("Команда", command, 8) { value -> command = value})

    override fun getLayoutRes(): Int = R.layout.item_text_input
}