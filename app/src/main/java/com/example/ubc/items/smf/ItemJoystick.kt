package com.example.ubc.items.smf

import android.util.Log
import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.KeyValuePair
import com.example.ubc.messageformats.smf.SMFBuilder
import kotlin.math.cos
import kotlin.math.sin

class ItemJoystick : Item() {
    private val rad : Float = (Math.PI / 180).toFloat()

    var command: String = "move"
    var angleToXY: Boolean = true
    var sendRate: Int = 100
    var centering: Boolean = true

    var joystickX: Int = 0
    var joystickY: Int = 0
    var joystickAngle: Int = 0
    var joystickStrength: Int = 0


    fun move(angle: Int, strength: Int) {
        if (angleToXY) {
            val radAngle = rad * angle
            joystickX = (strength * cos(radAngle)).toInt()
            joystickY = (strength * sin(radAngle)).toInt()
            Log.d("Joystick", "(x: $joystickX, y: $joystickY))")
        } else {
            joystickStrength = strength
            joystickAngle = 90 - angle
            if (joystickAngle < -180)
                joystickAngle += 360
            Log.d("Joystick", "(angle: $joystickAngle, strength: $joystickStrength))")
        }
    }

    fun getData() : ByteArray {
        if (angleToXY) {
            return SMFBuilder().putCommand(command).putIntCoordinates(joystickX, joystickY).build()
        } else {
            return SMFBuilder().putCommand(command).putIntCoordinates(joystickAngle, joystickStrength).build()
        }
    }

    override fun getParams() : List<ItemParam> = listOf(
            ItemParam.text("Команда", command) { value -> command = value},
            ItemParam.bool("Преобразовывать угол в (x,y)", angleToXY) { value -> angleToXY = value},
            ItemParam.integer("Интервал отправки данных, мс", sendRate, 50, 2000) { value -> sendRate = value},
            ItemParam.bool("Возвращать стик в центр", centering) { value -> centering = value}

    )

    override fun getParamValues(): List<KeyValuePair> = listOf(
            KeyValuePair("command", command),
            KeyValuePair("angleToXY", angleToXY.toString()),
            KeyValuePair("sendRate", sendRate.toString()),
            KeyValuePair("centering", centering.toString()),
        )

    override fun setParams(params: List<KeyValuePair>) {
        for (param in params) {
            when (param.key) {
                "command" -> command = param.value
                "angleToXY" -> angleToXY = param.value.toBoolean()
                "sendRate" -> sendRate = param.value.toInt()
                "centering" -> centering = param.value.toBoolean()
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.item_joystick
}