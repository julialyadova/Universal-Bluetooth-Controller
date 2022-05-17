package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import java.util.*

class ItemHistory : Item(){
    var maxRecords: Int = 20
    var fullWidth: Boolean = false

    val history = LinkedList<String>()

    fun onDataReceived(data: ByteArray) {
        history.add(String(data))
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