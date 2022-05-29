package com.example.ubc.data.repositories

import com.example.ubc.data.AppDatabase
import com.example.ubc.data.dao.PanelDao
import com.example.ubc.data.entities.PanelEntity
import javax.inject.Inject

class PanelsRepository @Inject constructor(
    db: AppDatabase
) {
    private val _panels : PanelDao = db.panelDao()

    suspend fun getAll() : List<PanelEntity> {
        return _panels.getAll();
    }

    suspend fun getById(id: Int) : PanelEntity {
        return _panels.getById(id)
    }

    suspend fun exists(id: Int) : Boolean {
        return _panels.exists(id)
    }

    suspend fun add(name: String) : Int {
        val panel = PanelEntity(id = 0, name = name)
        val id = _panels.add(panel).toInt()
        return id
    }

    suspend fun update(panel: PanelEntity) {
        _panels.update(panel)
    }

    suspend fun delete(id: Int) {
        getById(id)?.let {
            _panels.delete(it)
        }
    }
}