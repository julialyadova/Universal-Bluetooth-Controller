package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFReader

class ItemIndicator : Item() {
    var on_command: String = "on"
    var off_command: String = "off"
    var isOn: Boolean = false

    private val _reader = SMFReader()

    fun receiveData(data: ByteArray) {
        _reader.read(data)
        if (on_command == off_command) {
            _reader.whenCommand(on_command).doIfNoArgs { isOn = !isOn }
        } else {
            _reader.whenCommand(on_command).doIfNoArgs { isOn = true }
            _reader.whenCommand(off_command).doIfNoArgs { isOn = false }
        }
    }

    override fun getParams() : List<ItemParam> = listOf(
            ItemParam.text("Команда для ВКЛ", on_command) { value -> on_command = value},
            ItemParam.text("Команда для ВЫКЛ", off_command) { value -> off_command = value})

    override fun getParamValues(): List<KeyValuePair> = listOf(
            KeyValuePair("command_on", on_command),
            KeyValuePair("command_off", off_command))

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command_on" -> on_command = param.value
                "command_off" -> off_command = param.value
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_indicator
}