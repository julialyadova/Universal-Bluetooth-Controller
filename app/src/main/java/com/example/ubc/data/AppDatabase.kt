package com.example.ubc.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ubc.data.dao.ItemDao
import com.example.ubc.data.dao.PanelDao
import com.example.ubc.data.entities.ItemEntity
import com.example.ubc.data.entities.PanelEntity

@Database(entities = [PanelEntity::class, ItemEntity::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun panelDao(): PanelDao
    abstract fun itemDao(): ItemDao
}