package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemSwitch : Item() {
    var command_on: String = "on"
    var command_off: String = "off"

    init {
        addStoredParam("command_on", { command_on }, {command_on = it})
        addStoredParam("command_off", { command_off }, {command_off = it})
    }

    fun getOnData() : ByteArray{
        return SMFBuilder().putCommand(command_on).withNoArgs().build()
    }

    fun getOffData() : ByteArray{
        return SMFBuilder().putCommand(command_off).withNoArgs().build()
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
        StringParam("Отправить при ВКЛ", command_on, 8) { command_on = it},
        StringParam("Отправить при ВЫКЛ", command_off, 8) { command_off = it})

    override fun getLayoutRes(): Int = R.layout.item_switch
}