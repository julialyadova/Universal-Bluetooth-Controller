package com.example.ubc.items

abstract class Item () {
    var id: Int = 0
    var type: String = ""
    var panelId: Int = 0
    open var label: String = ""
    var x: Float = 0f
    var y: Float = 0f

    private val getters = mutableMapOf<String, () -> String>()
    private val setters = mutableMapOf<String, (String) -> Unit>()


    abstract fun getEditDialogParams() : List<ItemParam>

    protected fun addStoredParam(name: String, get: () -> String, set: (String) -> Unit) {
        getters[name] = get
        setters[name] = set
    }

    fun getParamValues() : List<KeyValuePair> {
        return getters.map { entry -> KeyValuePair(entry.key, entry.value()) }
    }
    fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            setters[param.key]?.invoke(param.value)
        }
    }

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