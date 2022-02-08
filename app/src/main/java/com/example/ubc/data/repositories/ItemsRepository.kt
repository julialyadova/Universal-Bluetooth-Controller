package com.example.ubc.data.repositories

import android.util.Log
import com.example.ubc.data.AppDatabase
import com.example.ubc.data.dao.ItemDao
import com.example.ubc.data.entities.Item
import javax.inject.Inject

class ItemsRepository @Inject constructor(
        db: AppDatabase
) {
    private val _items : ItemDao = db.itemDao()

    suspend fun findByPanelId(id: Int) : List<Item> {
        Log.d("ItemsRepository", "finding items for panel: $id")
        return _items.getAllByPanelId(id);
    }

    suspend fun save(item: Item): Int {
        var id = item.id
        if (id == 0) {
            id = _items.add(item).toInt()
            Log.d("ItemsRepository", "added new item: ${item.id} ${item.name}")
        } else {
            _items.update(item)
            Log.d("ItemsRepository", "item updated: ${item.id} ${item.name}")
        }
        return id
    }

    suspend fun delete(item: Item) {
        _items.delete(item)
        Log.d("ItemsRepository", "item deleted: ${item.id} ${item.name}")
    }
}