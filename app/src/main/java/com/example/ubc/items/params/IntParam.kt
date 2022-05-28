package com.example.ubc.items.params

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.example.ubc.items.ItemParam
import com.example.ubc.ui.shared.MinMaxInputFilter

class IntParam (
    name: String,
    var value: Int,
    var min: Int,
    var max: Int,
    var setter: (Int) -> Unit
) : ItemParam(name) {
    private lateinit var _editText: EditText

    override fun createView(context: Context): View {
        _editText = EditText(context).apply {
            setText(value.toString())
            inputType = EditorInfo.TYPE_CLASS_NUMBER
            filters = arrayOf(MinMaxInputFilter(min, max))
        }
        return _editText
    }

    override fun submit() {
        setter(_editText.text.toString().toInt())
    }
}