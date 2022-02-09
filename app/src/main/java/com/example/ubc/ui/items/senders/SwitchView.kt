package com.example.ubc.ui.items.senders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemSwitchBinding
import com.example.ubc.ui.editable.ViewShadowBuilder
import com.example.ubc.ui.items.SenderView
import com.example.ubc.ui.items.DataSender
import com.example.ubc.ui.main.dialogs.ControlDialog

class SwitchView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        dataSender: DataSender,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = item.id
) : SenderView(item, context, dataSender, attrs, defStyleAttr) {

    private val binding = ItemSwitchBinding.inflate(LayoutInflater.from(context),this,true)

    override fun getDragHandler() = binding.itemSwitch
    override fun getShadowBuilder() = ViewShadowBuilder(binding.root)
    override fun getCreateDialog() = ControlDialog()
    override fun getEditDialog() = ControlDialog()

    init {
        binding.itemSwitchLabel.text = item.label
        binding.itemSwitch.setOnCheckedChangeListener { _, isChecked ->
            send("switch ${item.label} state: $isChecked")
        }
    }
}