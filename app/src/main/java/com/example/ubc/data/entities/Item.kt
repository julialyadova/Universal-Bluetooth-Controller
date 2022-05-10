package com.example.ubc.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="panel_id")
    var panelId: Int = 0,

    var label: String,
    val type: String,
    var data: String,
    val format: String,

    @ColumnInfo(name="args")
    val argsJSON: String? = null,

    var x: Float = 0f,
    var y: Float = 0f,

    @ColumnInfo(name="display_label")
    val displayLabel: Boolean = false
) {
    class DataFormats {
        companion object {
            val HEX = "bytes"
            val ASCII = "ASCII"
        }
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