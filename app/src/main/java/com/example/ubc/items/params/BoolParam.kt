package com.example.ubc.items.params

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.example.ubc.items.ItemParam

class BoolParam (
    name: String,
    var value: Boolean,
    var setter: (Boolean) -> Unit
) : ItemParam(name) {
    private lateinit var _checkbox: CheckBox

    override fun createView(context: Context): View {
        _checkbox = CheckBox(context).apply {
            isChecked = value
        }
        return _checkbox
    }

    override fun submit() {
        setter(_checkbox.isChecked)
    }
}