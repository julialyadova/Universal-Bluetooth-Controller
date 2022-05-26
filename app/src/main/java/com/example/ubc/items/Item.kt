package com.example.ubc.items

abstract class Item () {
    var id: Int = 0
    var type: String = ""
    var panelId: Int = 0
    open var label: String = ""
    var x: Float = 0f
    var y: Float = 0f

    abstract fun getParams() : List<ItemParam>
    abstract fun getParamValues() : List<KeyValuePair>
    abstract fun setParams(params: List<KeyValuePair>)
    abstract fun getLayoutRes() : Int

    class Types {
        companion object {
            val BUTTON = "button"
            val SWITCH = "switch"
            val SIMPLE_DISPLAY = "simple_display"
            val HISTORY = "history"
            val SLIDER = "slider"
            val SCALE = "scale"
            var JOYSTICK = "joystick"
        }
    }
}