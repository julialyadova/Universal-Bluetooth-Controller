package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFReader
import java.util.*

class ItemHistory : Item(){
    var maxRecords: Int = 20
    var fullWidth: Boolean = false

    val history = LinkedList<String>()

    private val _reader = SMFReader()

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

    override fun getParams() : List<ItemParam> = listOf(
            ItemParam.integer("Лимит", maxRecords, 1, 100) { value -> maxRecords = value},
            ItemParam.bool("Растянуть по ширине экрана", fullWidth) { value -> fullWidth = value})

    override fun getParamValues(): List<KeyValuePair> = listOf(
            KeyValuePair("max_records", maxRecords.toString()),
            KeyValuePair("full_width", fullWidth.toString()))

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "max_records" -> maxRecords = param.value.toInt()
                "full_width" -> fullWidth = param.value.toBoolean()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_history
}