package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.IconParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemIconButton : Item() {
    var command: String = "btn"
    var icon: String = "default"

    init {
        addStoredParam("command", { command }, {command = it})
        addStoredParam("icon", { icon}, {icon = it})
    }

    fun getData() : ByteArray {
        return SMFBuilder().putCommand(command).withNoArgs().build()
    }

    override fun getEditDialogParams() : List<ItemParam> {
        return listOf(
            StringParam("Команда", command, 8) { command = it},
            IconParam("Иконка", icon) {icon = it}
        )
    }

    override fun getLayoutRes(): Int = R.layout.item_icon_button
}