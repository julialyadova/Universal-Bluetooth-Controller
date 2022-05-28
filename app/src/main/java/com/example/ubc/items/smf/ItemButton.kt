package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.ColorParam
import com.example.ubc.items.params.IconParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemButton : Item() {
    var command: String = "btn"
    var color: Int = 1

    init {
        addStoredParam("command", { command }, {command = it})
        addStoredParam("color", { color.toString() }, {color = it.toInt()})
    }

    fun getData() : ByteArray {
        return SMFBuilder().putCommand(command).withNoArgs().build()
    }

    override fun getEditDialogParams() : List<ItemParam> {
        return listOf(
            StringParam("Команда", command, 8) { command = it},
            ColorParam("Цвет", color) { color = it },
            IconParam("Иконка", "") {}
        )
    }

    override fun getLayoutRes(): Int = R.layout.item_button
}