package com.example.ubc.items.params

import android.content.Context
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.ubc.items.ItemParam

class CategoryParam (
    name: String,
    var value: String,
    var possibleValues: Array<String>,
    var setter: (String) -> Unit
) : ItemParam(name) {
    private lateinit var _radioGroup: RadioGroup

    override fun createView(context: Context): View {
        var selectedId = -1;
        _radioGroup = RadioGroup(context).apply {
            for (category in possibleValues) {
                val radio = RadioButton(context)
                radio.text = category
                if (category == value)
                    selectedId = radio.id
                addView(radio)
            }
            check(selectedId)
        }
        return _radioGroup
    }

    override fun submit() {
        val checkedButton = _radioGroup.findViewById<RadioButton>(_radioGroup.checkedRadioButtonId)
        setter(checkedButton.text.toString())
    }
}