package com.example.ubc.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ubc.data.dao.ItemDao
import com.example.ubc.data.dao.PanelDao
import com.example.ubc.data.entities.Item
import com.example.ubc.data.entities.Panel

@Database(entities = [Panel::class, Item::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun panelDao(): PanelDao
    abstract fun itemDao(): ItemDao
}