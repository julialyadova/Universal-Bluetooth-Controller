package com.example.ubc.items.smf

import android.util.Log
import com.example.ubc.R
import com.example.ubc.items.Item
import com.example.ubc.items.ItemParam
import com.example.ubc.items.params.BoolParam
import com.example.ubc.items.params.IntParam
import com.example.ubc.items.params.StringParam
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

    init {
        addStoredParam("command", { command }, {command = it})
        addStoredParam("angleToXY", { angleToXY.toString() }, {angleToXY = it.toBoolean()})
        addStoredParam("sendRate", { sendRate.toString() }, {sendRate = it.toInt()})
        addStoredParam("centering", { centering.toString() }, {centering = it.toBoolean()})
    }


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

    override fun getEditDialogParams() : List<ItemParam> = listOf(
            StringParam("Команда", command, 8) { command = it},
            BoolParam("Преобразовывать угол в (x,y)", angleToXY) { angleToXY = it},
            IntParam("Интервал отправки данных, мс", sendRate, 50, 2000) { sendRate = it},
            BoolParam("Возвращать стик в центр", centering) { centering = it}

    )

    override fun getLayoutRes(): Int = R.layout.item_joystick
}