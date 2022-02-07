package com.example.ubc.data.dao

import androidx.room.*
import com.example.ubc.data.entities.Control

@Dao
interface ControlDao {

    @Query("SELECT * FROM Control WHERE panel_id = :id")
    fun getAllByPanelId(id: Int) : List<Control>

    @Insert()
    fun add(control: Control) : Long

    @Update()
    fun update(control: Control)

    @Delete()
    fun delete(control: Control)
}
