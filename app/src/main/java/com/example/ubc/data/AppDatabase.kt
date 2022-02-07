package com.example.ubc.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ubc.data.dao.ControlDao
import com.example.ubc.data.dao.PanelDao
import com.example.ubc.data.entities.Control
import com.example.ubc.data.entities.Panel

@Database(entities = [Panel::class, Control::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun panelDao(): PanelDao
    abstract fun controlDao(): ControlDao
}