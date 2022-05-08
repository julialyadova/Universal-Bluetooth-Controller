package com.example.ubc.ui.items

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.ubc.data.entities.Item
import com.example.ubc.ui.items.displays.HistoryView
import com.example.ubc.ui.items.displays.SimpleDisplayView
import com.example.ubc.ui.items.senders.ButtonView
import com.example.ubc.ui.items.senders.SwitchView
import com.example.ubc.ui.main.viewmodels.ControlPanelViewModel

class ItemViewFactory(
        val context: Context,
        private val viewModel: ControlPanelViewModel,
        private val lifecycleOwner : LifecycleOwner) {

    fun create(item: Item): ItemView? {
        Log.d("ItemsFactory", "creating item view of type ${item.type}(${item.id}) ${item.x} ${item.y}")
        val itemView = when(item.type) {
            Item.Types.BUTTON -> ButtonView(item, context, viewModel)
            Item.Types.SWITCH -> SwitchView(item, context, viewModel)
            Item.Types.SIMPLE_DISPLAY -> SimpleDisplayView(item, context)
            Item.Types.HISTORY -> HistoryView(item, context)
            else -> null
        }
        if (itemView is DisplayView)
            viewModel.received.observe(lifecycleOwner) { data ->
                itemView.receive(data)
            }
        return itemView
    }
}