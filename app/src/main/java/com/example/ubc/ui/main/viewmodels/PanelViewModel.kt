package com.example.ubc.ui.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ubc.data.entities.Panel
import com.example.ubc.data.repositories.PanelsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PanelViewModel @Inject constructor(
        private val _panels: PanelsRepository,
) : ViewModel() {
    val panel = MutableLiveData<Panel>()

    fun load(id: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val p = _panels.getById(id)
            withContext(Dispatchers.Main) {
                panel.value = p
            }
        }
    }

    fun create(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val id = _panels.add(name)
            val p = _panels.getById(id)
            withContext(Dispatchers.Main) {
                panel.value = p
            }
        }
    }

    fun rename(name: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val p = panel.value!!
            p.name = name;
            _panels.update(p)
            withContext(Dispatchers.Main) {
                panel.value = p
            }
        }
    }
}