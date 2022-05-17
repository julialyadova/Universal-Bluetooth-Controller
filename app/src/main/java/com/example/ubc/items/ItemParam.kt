package com.example.ubc.items

class ItemParam (
    val type: ParamType,
    val name: String,
    var value: String,
    var set: (String) -> Unit,
) {
    var valuesList : List<String> = listOf()
    var min : Float = 0.0f
    var max: Float = 0.0f

    companion object {
        fun text(name: String, value: String, setter: (String) -> Unit) : ItemParam {
            return ItemParam(ParamType.TEXT, name, value, setter)
        }

        fun integer(name: String, value: Int,
                    minValue: Int, maxValue: Int,
                    setter: (Int) -> Unit) : ItemParam {
            return ItemParam(ParamType.INTEGER, name, value.toString()) { stringValue ->
                setter(stringValue.toInt())
            }.apply {
                min = minValue.toFloat()
                max = maxValue.toFloat()
            }
        }

        fun float(name: String, value: Float,
                  minValue: Float, maxValue: Float,
                  setter: (Float) -> Unit) : ItemParam {
            return ItemParam(ParamType.DECIMAL, name, value.toString()) { stringValue ->
                setter(stringValue.toFloat())
            }.apply {
                min = minValue
                max = maxValue
            }
        }

        fun bool(name: String, value: Boolean,
                 setter: (Boolean) -> Unit) : ItemParam {
            return ItemParam(ParamType.FLAG, name, value.toString()) { stringValue ->
                setter(stringValue.toBoolean())
            }
        }

        fun category(name: String, value: String,
                     possibleValues: List<String>, setter: (String) -> Unit) : ItemParam {
            return ItemParam(ParamType.CATEGORY, name, value, setter).apply {
                valuesList = possibleValues
            }
        }
    }
}

enum class ParamType {
    TEXT,
    INTEGER,
    DECIMAL,
    CATEGORY,
    FLAG
}