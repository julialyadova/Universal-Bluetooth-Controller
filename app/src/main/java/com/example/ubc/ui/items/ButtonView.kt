package com.example.ubc.ui.items

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.R
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemControlBinding
import com.example.ubc.ui.main.dialogs.ControlDialog

class ButtonView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = item.id
) : ItemView(item, context, attrs, defStyleAttr) {

    private val binding = ItemControlBinding.inflate(LayoutInflater.from(context),this,true)

    override fun getItemId() = item.id
    override fun getCreateDialog() = ControlDialog()
    override fun getEditDialog() = ControlDialog()
    override fun getDragHandler() = binding.controlText


    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    init {
        binding.controlText.text = "test text"
    }
}