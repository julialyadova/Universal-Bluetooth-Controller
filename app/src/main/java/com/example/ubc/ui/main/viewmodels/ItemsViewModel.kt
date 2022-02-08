package com.example.ubc.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.entities.Item
import com.example.ubc.data.repositories.ItemsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ItemsViewModel @Inject constructor(
        private val _db: ItemsRepository,
) : ViewModel() {

    val items = MutableLiveData<List<Item>>()

    fun loadForPanel(id: Int) {
        Log.d("ItemsViewModel", "loading items for panel: $id")
        GlobalScope.launch(Dispatchers.IO) {
            val i = _db.findByPanelId(id)
            withContext(Dispatchers.Main) {
                items.value = i
                Log.d("ItemsViewModel", "items loaded: ${i.size}")
            }
        }
    }

    fun save(item: Item) {
        Log.d("ItemsViewModel", "save item ${item.id} ${item.name}")
        GlobalScope.launch(Dispatchers.IO) {
            _db.save(item)
            withContext(Dispatchers.Main) {
                items.value = items.value
            }
        }
    }

    fun setPosition(id: Int, x: Int, y: Int) {
        Log.d("ItemsViewModel", "move item $id to ($x; $y)")
        GlobalScope.launch(Dispatchers.IO) {
            val item = items.value?.firstOrNull()
            if (item != null) {
                //item.data
                _db.save(item)
                withContext(Dispatchers.Main) {
                    items.value = items.value
                }
            }
        }
    }

    fun delete(item: Item) {
        Log.d("ItemsViewModel", "delete item ${item.id} ${item.name}")
        GlobalScope.launch(Dispatchers.IO) {
            _db.delete(item)
            withContext(Dispatchers.Main) {
                items.value = items.value?.filter { i -> i != item }
            }
        }
    }
}