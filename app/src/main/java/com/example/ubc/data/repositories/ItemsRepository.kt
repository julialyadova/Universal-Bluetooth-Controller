package com.example.ubc.data.repositories

import android.util.Log
import com.example.ubc.data.AppDatabase
import com.example.ubc.data.dao.ItemDao
import com.example.ubc.data.entities.ItemEntity
import javax.inject.Inject

class ItemsRepository @Inject constructor(
        db: AppDatabase
) {
    private val _items : ItemDao = db.itemDao()

    suspend fun findByPanelId(id: Int) : List<ItemEntity> {
        Log.d("ItemsRepository", "finding items for panel: $id")
        return _items.getAllByPanelId(id);
    }

    suspend fun getById(id: Int) : ItemEntity? {
        return _items.getById(id)
    }

    suspend fun insert(item: ItemEntity): Int {
        return _items.add(item).toInt()
    }

    suspend fun update(item: ItemEntity) {
        _items.update(item)
    }

    suspend fun delete(item: ItemEntity) {
        _items.delete(item)
        Log.d("ItemsRepository", "item deleted: ${item.id} ${item.label}")
    }
}