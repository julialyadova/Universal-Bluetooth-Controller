package com.example.ubc.ui.items.senders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemSwitchBinding
import com.example.ubc.ui.editor.ViewShadowBuilder
import com.example.ubc.ui.items.SenderView
import com.example.ubc.ui.main.dialogs.ItemDialog
import com.example.ubc.ui.main.viewmodels.ItemViewModel

class SwitchView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        viewModel: ItemViewModel,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = item.id
) : SenderView(item, context, viewModel, attrs, defStyleAttr) {

    private val binding = ItemSwitchBinding.inflate(LayoutInflater.from(context),this,true)

    override fun getDragHandler() = binding.itemSwitch
    override fun getShadowBuilder() = ViewShadowBuilder(binding.root)
    override fun getCreateDialog() = ItemDialog()
    override fun getEditDialog() = ItemDialog()

    init {
        binding.itemSwitchLabel.text = item.label
        binding.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            send("switch ${item.label} state: $isChecked".toByteArray())
        }
    }
}