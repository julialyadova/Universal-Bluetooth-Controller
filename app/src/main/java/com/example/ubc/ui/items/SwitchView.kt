package com.example.ubc.ui.items

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemSwitchBinding
import com.example.ubc.ui.main.dialogs.ControlDialog

class SwitchView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemSwitchBinding.inflate(LayoutInflater.from(context),this,true)

    override fun getItemId() = item.id
    override fun getCreateDialog() = ControlDialog()
    override fun getEditDialog() = ControlDialog()
    override fun getDragHandler() = binding.itemSwitch


    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    init {
        binding.itemSwitchLabel.text = item.name
    }
}