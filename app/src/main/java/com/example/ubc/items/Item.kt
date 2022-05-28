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
            const val BUTTON = "button"
            const val SWITCH = "switch"
            const val SIMPLE_DISPLAY = "simple_display"
            const val HISTORY = "history"
            const val SLIDER = "slider"
            const val SCALE = "scale"
            const val ROUND_SCALE = "round_scale"
            const val JOYSTICK = "joystick"
            const val INDICATOR = "indicator"
            const val TEXT_INPUT = "text_input"
        }
    }
}