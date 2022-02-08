package com.example.ubc.data.repositories

import com.example.ubc.data.AppDatabase
import com.example.ubc.data.dao.PanelDao
import com.example.ubc.data.entities.Panel
import javax.inject.Inject

class PanelsRepository @Inject constructor(
    db: AppDatabase
) {
    private val _panels : PanelDao = db.panelDao()

    suspend fun getAll() : List<Panel> {
        return _panels.getAll();
    }

    suspend fun getById(id: Int) : Panel {
        return _panels.getById(id)
    }

    suspend fun add(name: String) : Int {
        val panel = Panel(id = 0, name = name)
        val id = _panels.add(panel).toInt()
        return id
    }

    suspend fun update(panel: Panel) {
        _panels.update(panel)
    }

    suspend fun delete(panel: Panel) {
        _panels.delete(panel)
    }
}