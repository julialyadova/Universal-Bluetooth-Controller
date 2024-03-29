package com.example.ubc.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PanelEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String,
    var isHorizontal: Boolean = false
)
