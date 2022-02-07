package com.example.ubc.data.dao

import androidx.room.*
import com.example.ubc.data.entities.Panel

@Dao
interface PanelDao {
    @Query("SELECT * FROM Panel")
    fun getAll(): List<Panel>

    @Query("SELECT * FROM Panel WHERE id = :id LIMIT 1")
    fun getById(id: Int) : Panel

    @Insert()
    fun add(panel: Panel) : Long

    @Update()
    fun update(panel: Panel)

    @Delete()
    fun delete(panel: Panel)
}
