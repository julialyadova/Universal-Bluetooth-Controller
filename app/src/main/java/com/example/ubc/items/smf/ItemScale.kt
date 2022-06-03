package com.example.ubc.items.smf

import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.BoolParam
import com.example.ubc.items.params.IntParam
import com.example.ubc.items.params.StringParam
import com.example.ubc.messageformats.smf.SMFReader

open class ItemScale : Item() {
    override var label = "шкала"
    var command: String = "scale"
    var showValue: Boolean = true
    var min: Int = 0
    var max: Int = 255
    var value: Int = 0
        private set

    private val _reader = SMFReader()

    init {
        addStoredParam("command", { command }, {command = it})
        addStoredParam("min", { min.toString() }, {min = it.toInt()})
        addStoredParam("max", { max.toString() }, { max = it.toInt() })
        addStoredParam("show_val", { showValue.toString() }, { showValue = it.toBoolean() })
    }

    fun receiveData(data: ByteArray) {
        _reader.read(data).whenCommand(command).doIfIntArg {
            value = it.coerceIn(min, max)
        }
    }

    override fun getEditDialogParams() : List<ItemParam> = listOf(
        StringParam("Команда", command, 8) { command = it},
        IntParam("MIN", min, 0, 254) { min = it},
        IntParam("MAX", max, 1, 255) { max = it.coerceAtLeast(min + 1) },
        BoolParam("Показывать значение", showValue) { showValue = it }
    )

    override fun getLayoutRes(): Int = R.layout.item_slider
}