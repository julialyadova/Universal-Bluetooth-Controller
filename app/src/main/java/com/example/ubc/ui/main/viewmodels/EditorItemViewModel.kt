package com.example.ubc.ui.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ubc.data.ControlPanelService
import com.example.ubc.data.entities.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorItemViewModel @Inject constructor(
        private val _service: ControlPanelService,
) : ViewModel() {

    fun update(item: Item) {
        Log.d("ItemsViewModel", "save item ${item.id} ${item.label}")
        GlobalScope.launch(Dispatchers.IO) {
            _service.updateItem(item)
        }
    }

    fun delete(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val p = _service.deleteItem(id)
        }
    }
}