package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFReader

class ItemDisplay : Item() {
    var command: String = "display"
    var displayedValue: String = ""

    private val _reader = SMFReader()

    init {
        addStoredParam("command", {command}, {command = it})
    }

    fun receiveData(data: ByteArray) {
        _reader.read(data).whenCommand(command)
        _reader.doIfIntArg { displayedValue = it.toString() }
        _reader.doIfFloatArg { displayedValue = it.toString() }
        _reader.doIfStringArg { displayedValue = it }
        _reader.doIfIntCoordinatesArgs { x, y -> displayedValue = "($x,$y)" }
        _reader.doIfFloatCoordinatesArgs { x, y -> displayedValue = "($x,$y)" }
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
        StringParam("Идентификатор", command, 8) { value -> command = value.replace(' ','_')})

    override fun getLayoutRes(): Int = R.layout.item_simple_display
}