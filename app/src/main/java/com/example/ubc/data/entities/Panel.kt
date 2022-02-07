package com.example.ubc.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Panel (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var name: String
)
