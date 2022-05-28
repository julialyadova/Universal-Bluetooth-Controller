package com.example.ubc.items.params

import android.content.Context
import android.text.InputFilter
import android.view.View
import android.widget.EditText
import com.example.ubc.items.ItemParam

class StringParam (
    name: String,
    var value: String,
    var maxLength: Int,
    var setter: (String) -> Unit
) : ItemParam(name) {
    private lateinit var _editText: EditText

    override fun createView(context: Context) : View {
        _editText = EditText(context).apply {
            setText(value)
            filters = arrayOf(InputFilter.LengthFilter(maxLength))
        }
        return _editText
    }

    override fun submit() {
        setter(_editText.text.toString().take(maxLength))
    }
}