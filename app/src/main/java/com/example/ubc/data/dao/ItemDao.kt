package com.example.ubc.data.dao

import androidx.room.*
import com.example.ubc.data.entities.ItemEntity

@Dao
interface ItemDao {

    @Query("SELECT * FROM ItemEntity WHERE id = :id")
    fun getById(id: Int) : ItemEntity?

    @Query("SELECT * FROM ItemEntity WHERE panel_id = :id")
    fun getAllByPanelId(id: Int) : List<ItemEntity>

    @Insert()
    fun add(item: ItemEntity) : Long

    @Update()
    fun update(item: ItemEntity)

    @Delete()
    fun delete(item: ItemEntity)
}
