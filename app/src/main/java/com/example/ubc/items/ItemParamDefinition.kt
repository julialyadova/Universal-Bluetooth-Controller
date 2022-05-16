package com.example.ubc.items

class ItemParamDefinition (
        val key: String,
        val type: ParamType,
        val defaultValue: String
) {
    fun toText() : String {
        return defaultValue
    }

    fun toDecimal() : Int {
        return defaultValue.toInt()
    }

    fun toFloat() : Float {
        return defaultValue.toFloat()
    }

    fun toCategories() : List<String> {
        return defaultValue.split(",")
    }

    fun toFlag() : Boolean {
        return defaultValue.toBoolean()
    }
}

enum class ParamType {
    TEXT,
    DECIMAL,
    FLOAT,
    CATEGORY,
    FLAG
}