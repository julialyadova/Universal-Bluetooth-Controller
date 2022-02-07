package com.example.ubc.data.repositories

import com.example.ubc.data.AppDatabase
import com.example.ubc.data.dao.ControlDao
import com.example.ubc.data.dao.PanelDao
import com.example.ubc.data.entities.Control
import com.example.ubc.data.entities.Panel
import javax.inject.Inject

class ControlPanelRepository @Inject constructor(
    db: AppDatabase
) {
    private val _panels : PanelDao = db.panelDao()
    private val _controls : ControlDao = db.controlDao()

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

    suspend fun getControls(id: Int) : List<Control> {
        return _controls.getAllByPanelId(id);
    }

    suspend fun saveControl(control: Control): Int {
        var id = control.id
        if (id == 0) {
            id = _controls.add(control).toInt()
        } else {
            _controls.update(control)
        }
        return id
    }

    suspend fun deleteControl(control: Control) {
        _controls.delete(control)
    }
}