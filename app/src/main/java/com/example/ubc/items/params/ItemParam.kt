package com.example.ubc.items

import android.content.Context
import android.view.View

abstract class ItemParam (var name: String) {

    abstract fun submit()
    abstract fun createView(context: Context) : View

    enum class Type {
        TEXT,
        INTEGER,
        DECIMAL,
        BOOL,
        CATEGORY,
        DRAWABLE_RES,
        COLOR
    }
}