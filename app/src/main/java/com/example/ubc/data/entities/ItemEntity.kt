package com.example.ubc.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "panel_id")
    var panelId: Int = 0,
    var label: String = "",
    val type: String,
    var data: String = "[]",
    var x: Float = 0f,
    var y: Float = 0f,
    @ColumnInfo(name = "display_label")
    var displayLabel: Boolean = false
)