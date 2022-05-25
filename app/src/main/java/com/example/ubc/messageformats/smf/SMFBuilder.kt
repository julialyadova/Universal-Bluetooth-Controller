package com.example.ubc.messageformats.smf

import com.example.ubc.messageformats.BytesConverter

class SMFBuilder {
    private var _buffer = ByteArray(SMF.MAX_MESSAGE_SIZE)
    private var argsSize: Byte = 0
    private var argsType: Byte = 0

    fun putCommand(command: String) : SMFBuilder {
        val length = Math.min(command.length, SMF.BYTES_COMMAND_SIZE)
        command.toByteArray()
                .copyInto(_buffer, SMF.BYTES_COMMAND_INDEX,0, length)
        return this
    }

    fun withNoArgs() : SMFBuilder {
        argsSize = 0
        argsType = SMF.ARGS_TYPE_NO_ARGS
        return this
    }

    fun putInt(value: Int) : SMFBuilder {
        BytesConverter.writeInt16(_buffer, SMF.BYTES_ARGS_INDEX, value)
        argsSize = 4
        argsType = SMF.ARGS_TYPE_INT
        return this
    }

    fun putFloat(value: Float) : SMFBuilder {
        BytesConverter.writeFloat(_buffer, SMF.BYTES_ARGS_INDEX, value)
        argsSize = 4
        argsType = SMF.ARGS_TYPE_FLOAT
        return this
    }

    fun putString(value: String) : SMFBuilder {
        argsSize = Math.min(value.length, SMF.MAX_ARGS_SIZE).toByte()
        argsType = SMF.ARGS_TYPE_STRING
        value.toByteArray().copyInto(_buffer, SMF.BYTES_ARGS_INDEX, 0, argsSize.toInt())
        return this
    }

    fun putIntCoordinates(x: Int, y: Int) : SMFBuilder {
        BytesConverter.writeInt16(_buffer, SMF.BYTES_ARGS_INDEX, x)
        BytesConverter.writeInt16(_buffer, SMF.BYTES_ARGS_INDEX + 4, y)
        argsSize = 8
        argsType = SMF.ARGS_TYPE_INT_COORDINATES
        return this
    }

    fun putFloatCoordinates(x: Float, y: Float) : SMFBuilder {
        BytesConverter.writeFloat(_buffer, SMF.BYTES_ARGS_INDEX, x)
        BytesConverter.writeFloat(_buffer, SMF.BYTES_ARGS_INDEX + 4, y)
        argsSize = 8
        argsType = SMF.ARGS_TYPE_FLOAT_COORDINATES
        return this
    }

    fun build() : ByteArray {
        val size = (SMF.MIN_MESSAGE_SIZE + argsSize)
        _buffer[SMF.BYTES_SIZE_INDEX] = size.toByte()
        _buffer[SMF.BYTES_ARGS_TYPE_INDEX] = argsType
        return _buffer.copyOf(size)
    }
}