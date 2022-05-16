package com.example.ubc.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Item (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "panel_id")
    var panelId: Int = 0,
    var label: String = "",
    var type: String = "",
    var x: Float = 0f,
    var y: Float = 0f,
    @ColumnInfo(name = "display_label")
    var displayLabel: Boolean = false
) {
    var data: String = ""
        set(value) {
            value.split(";")
                .map { s -> s.split("=") }
                .filter { s -> s.size == 2 }
                .forEach {pair -> params[pair[0]] = pair[1] }
            field = value
        }
    @Ignore
    var params: MutableMap<String, String> = mutableMapOf()

    fun setParam(key: String, value: String) {
        params[key] = value
        val values = params.map { pair -> "${pair.key}=${pair.value}" }
        data = if (values.size == 1)
            values[0]
        else
            values.joinToString(";")
    }

    class Types {
        companion object {
            val BUTTON = "button"
            val SWITCH = "switch"
            val SIMPLE_DISPLAY = "simple_display"
            val HISTORY = "history"
        }
    }
}