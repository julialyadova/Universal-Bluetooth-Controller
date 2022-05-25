package com.example.ubc.messageformats

import java.nio.ByteBuffer

class BytesConverter {
    companion object {
        const val NULL_CHAR = 0.toChar()

        fun readInt16(array: ByteArray, index: Int): Int {
            return ByteBuffer.allocate(4)
                    .put(array, index, 4)
                    .getInt(0)
        }

        fun writeInt16(array: ByteArray, index: Int, data: Int) {
            ByteBuffer.allocate(4)
                    .putInt(data)
                    .array().copyInto(array, index)
        }

        fun readFloat(array: ByteArray, index: Int): Float {
            return ByteBuffer.allocate(4)
                    .put(array, index, 4)
                    .getFloat(0)
        }

        fun writeFloat(array: ByteArray, index: Int, data: Float) {
            ByteBuffer.allocate(4)
                    .putFloat(data)
                    .array().copyInto(array, index)
        }
    }
}