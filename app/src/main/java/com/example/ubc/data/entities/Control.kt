package com.example.ubc.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Control (
    val name: String,
    val data: String,
    //val timer: Int? = null,
    //val format:

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name="panel_id")
    var panelId: Int = 0
)

//кнопка с текстом и данными по отправке   + дисплей
//таймер с текстом и данными + задержкой   + дисплей

//timer = null 200 500
