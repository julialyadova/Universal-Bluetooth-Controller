package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.IntParam
import com.example.ubc.messageformats.smf.SMFReader
import java.util.*

class ItemHistory : Item(){
    var maxRecords: Int = 20
    val history = LinkedList<String>()
    private val _reader = SMFReader()

    init {
        addStoredParam("max_records", { maxRecords.toString() }, {maxRecords = it.toInt()})
    }

    fun onDataReceived(data: ByteArray) {
        _reader.read(data)
        val command = _reader.readCommand()
        var args = ""
        _reader.doIfIntArg { args = it.toString() }
        _reader.doIfFloatArg { args = it.toString() }
        _reader.doIfStringArg { args = it }
        _reader.doIfIntCoordinatesArgs { x, y -> args = "($x,$y)" }
        _reader.doIfFloatCoordinatesArgs { x, y -> args = "($x,$y)" }
        _reader.doIfIntArg {  }

        history.add("$command : $args ")
        if (history.size > maxRecords) {
            history.remove()
        }
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
            IntParam("Лимит", maxRecords, 1, 100) { value -> maxRecords = value})

    override fun getLayoutRes(): Int = R.layout.item_history
}