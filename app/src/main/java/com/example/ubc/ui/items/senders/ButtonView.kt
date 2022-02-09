package com.example.ubc.ui.items.senders

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.ubc.data.entities.Item
import com.example.ubc.databinding.ItemControlBinding
import com.example.ubc.ui.editable.ViewShadowBuilder
import com.example.ubc.ui.items.DataSender
import com.example.ubc.ui.items.SenderView
import com.example.ubc.ui.main.dialogs.ControlDialog

class ButtonView @JvmOverloads constructor(
        private val item: Item,
        context: Context,
        dataSender: DataSender,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = item.id
) : SenderView(item, context, dataSender, attrs, defStyleAttr) {

    private val binding = ItemControlBinding.inflate(LayoutInflater.from(context),this,true)

    override fun getDragHandler() = binding.controlText
    override fun getShadowBuilder() = ViewShadowBuilder(binding.root)
    override fun getCreateDialog() = ControlDialog()
    override fun getEditDialog() = ControlDialog()

    init {
        binding.controlText.text = "test text"
        binding.controlText.setOnClickListener {
            send("button clicked")
        }
    }
}