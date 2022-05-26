package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFBuilder

class ItemSwitch : Item() {
    var command_on: String = "on"
    var command_off: String = "off"

    fun getOnData() : ByteArray{
        return SMFBuilder().putCommand(command_on).withNoArgs().build()
    }

    fun getOffData() : ByteArray{
        return SMFBuilder().putCommand(command_off).withNoArgs().build()
    }

    override fun getParams() : List<ItemParam> = listOf(
        ItemParam.text("Отправить при ВКЛ", command_on) { value -> command_on = value},
        ItemParam.text("Отправить при ВЫКЛ", command_off) { value -> command_off = value})

    override fun getParamValues(): List<KeyValuePair> = listOf(
            KeyValuePair("command_on", command_on),
            KeyValuePair("command_off", command_off))

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command_on" -> command_on = param.value
                "command_off" -> command_off = param.value
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_switch
}