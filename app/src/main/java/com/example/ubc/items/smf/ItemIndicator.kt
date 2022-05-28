package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFReader

class ItemIndicator : Item() {
    var on_command: String = "on"
    var off_command: String = "off"
    var isOn: Boolean = false

    private val _reader = SMFReader()

    init {
        addStoredParam("command_on", { on_command }, {off_command = it})
        addStoredParam("command_off", { off_command }, {off_command = it})
    }

    fun receiveData(data: ByteArray) {
        _reader.read(data)
        if (on_command == off_command) {
            _reader.whenCommand(on_command).doIfNoArgs { isOn = !isOn }
        } else {
            _reader.whenCommand(on_command).doIfNoArgs { isOn = true }
            _reader.whenCommand(off_command).doIfNoArgs { isOn = false }
        }
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
        StringParam("Команда для ВКЛ", on_command, 8) { on_command = it},
        StringParam("Команда для ВЫКЛ", off_command, 8) { off_command = it})

    override fun getLayoutRes(): Int = R.layout.item_indicator
}