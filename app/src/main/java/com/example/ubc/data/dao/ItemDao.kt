package com.example.ubc.data.dao

import androidx.room.*
import com.example.ubc.data.entities.Item

@Dao
interface ItemDao {

    @Query("SELECT * FROM Item WHERE id = :id")
    fun getById(id: Int) : Item?

    @Query("SELECT * FROM Item WHERE panel_id = :id")
    fun getAllByPanelId(id: Int) : List<Item>

    @Insert()
    fun add(item: Item) : Long

    @Update()
    fun update(item: Item)

    @Delete()
    fun delete(item: Item)
}
