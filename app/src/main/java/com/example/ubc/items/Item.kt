package com.example.ubc.items

abstract class Item () {
    var id: Int = 0
    var panelId: Int = 0
    open var label: String = ""
    var x: Float = 0f
    var y: Float = 0f

    abstract fun getParams() : List<ItemParam>
    abstract fun getParamValues() : List<KeyValuePair>
    abstract fun setParams(params: List<KeyValuePair>)
    abstract fun getLayoutRes() : Int
}