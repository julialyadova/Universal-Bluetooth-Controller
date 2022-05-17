package com.example.ubc.data.dao

import androidx.room.*
import com.example.ubc.data.entities.PanelEntity

@Dao
interface PanelDao {
    @Query("SELECT * FROM PanelEntity")
    fun getAll(): List<PanelEntity>

    @Query("SELECT * FROM PanelEntity WHERE id = :id LIMIT 1")
    fun getById(id: Int) : PanelEntity

    @Insert()
    fun add(panel: PanelEntity) : Long

    @Update()
    fun update(panel: PanelEntity)

    @Delete()
    fun delete(panel: PanelEntity)
}
