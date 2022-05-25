package com.example.ubc.messageformats.smf

import com.example.ubc.messageformats.BytesConverter

class SMFReader {
    private var _message: ByteArray = byteArrayOf()
    private var _valid: Boolean = false

    private val _messageLength: Int
        get() = _message[SMF.BYTES_SIZE_INDEX].toInt()

    private val _messageArgsType: Byte
        get() = _message[SMF.BYTES_ARGS_TYPE_INDEX]

    private val _messageCommand: String
        get() = String(_message, SMF.BYTES_COMMAND_INDEX, SMF.BYTES_COMMAND_SIZE).trimEnd(
            BytesConverter.NULL_CHAR
        )

    fun withMessage(message: ByteArray) : SMFReader {
        _message = message
        return this
    }

    fun whenCommand(command: String) : SMFReader {
        _valid = _message.size >= SMF.MIN_MESSAGE_SIZE
                && _message.size ==_messageLength
                && _messageCommand == command
        return this
    }

    fun doIfNoArgs(action: () -> Unit) {
        if (_valid && _messageArgsType == SMF.ARGS_TYPE_NO_ARGS)
            action.invoke()
    }

    fun doIfIntArg(action: (Int) -> Unit) {
        if (_valid && _messageArgsType == SMF.ARGS_TYPE_INT && _message.size >= SMF.BYTES_ARGS_INDEX + 4) {
            val arg = BytesConverter.readInt16(_message, SMF.BYTES_ARGS_INDEX)
            action.invoke(arg)
        }
    }

    fun doIfFloatArg(action: (Float) -> Unit) {
        if (_valid && _messageArgsType == SMF.ARGS_TYPE_FLOAT && _message.size >= SMF.BYTES_ARGS_INDEX + 4) {
            val arg = BytesConverter.readFloat(_message, SMF.BYTES_ARGS_INDEX)
            action.invoke(arg)
        }
    }

    fun doIfStringArg(action: (String) -> Unit) {
        if (_valid && _messageArgsType == SMF.ARGS_TYPE_STRING) {
            val arg = String(_message, 10, _message.size - 10)
            action.invoke(arg)
        }
    }

    fun doIfIntCoordinatesArgs(action: (Int, Int) -> Unit) {
        if (_valid
            && _messageArgsType == SMF.ARGS_TYPE_INT_COORDINATES
            && _message.size >= SMF.BYTES_ARGS_INDEX + 8) {

            val x = BytesConverter.readInt16(_message, SMF.BYTES_ARGS_INDEX)
            val y = BytesConverter.readInt16(_message, SMF.BYTES_ARGS_INDEX + 4)
            action.invoke(x, y)
        }
    }

    fun doIfFloatCoordinatesArgs(action: (Float, Float) -> Unit) {
        if (_valid
            && _messageArgsType == SMF.ARGS_TYPE_FLOAT_COORDINATES
            && _message.size >= SMF.BYTES_ARGS_INDEX + 8) {

            val x = BytesConverter.readFloat(_message, SMF.BYTES_ARGS_INDEX)
            val y = BytesConverter.readFloat(_message, SMF.BYTES_ARGS_INDEX + 4)
            action.invoke(x, y)
        }
    }
}