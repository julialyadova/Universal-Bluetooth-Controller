package com.example.ubc.messageformats.smf

class SMF {
    companion object {
        const val MIN_MESSAGE_SIZE = 10
        const val MAX_MESSAGE_SIZE = 128
        const val MAX_ARGS_SIZE = MAX_MESSAGE_SIZE - MIN_MESSAGE_SIZE

        const val BYTES_SIZE_INDEX = 0
        const val BYTES_ARGS_TYPE_INDEX = 1
        const val BYTES_COMMAND_INDEX = 2
        const val BYTES_COMMAND_SIZE = 8
        const val BYTES_ARGS_INDEX = 10

        const val ARGS_TYPE_NO_ARGS: Byte = 0
        const val ARGS_TYPE_INT: Byte = 1
        const val ARGS_TYPE_FLOAT: Byte = 2
        const val ARGS_TYPE_STRING: Byte = 3
        const val ARGS_TYPE_INT_COORDINATES: Byte = 4
        const val ARGS_TYPE_FLOAT_COORDINATES: Byte = 5
    }
}