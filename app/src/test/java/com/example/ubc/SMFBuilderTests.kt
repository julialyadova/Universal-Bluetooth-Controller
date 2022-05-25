package com.example.ubc

import com.example.ubc.messageformats.BytesConverter
import com.example.ubc.messageformats.smf.SMF
import com.example.ubc.messageformats.smf.SMFBuilder
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SMFBuilderTests {
    @Test
    fun put_command() {
        val message = SMFBuilder()
                .putCommand("test")
                .build()
        val command = String(message, SMF.BYTES_COMMAND_INDEX, SMF.BYTES_COMMAND_SIZE).trim(BytesConverter.NULL_CHAR)
        assertEquals(4, 2 + 2)
    }

    @Test
    fun command_int() {
        val expected = byteArrayOf(
                14,
                SMF.ARGS_TYPE_INT,
                'i'.toByte(), 'n'.toByte(), 't'.toByte(), 't'.toByte(), 'e'.toByte(), 's'.toByte(), 't'.toByte(), 0,
                0,0,0,12
        )

        val actual = SMFBuilder()
                .putCommand("inttest")
                .putInt(12)
                .build()

        assertEquals(expected.joinToString(" "), actual.joinToString(" "))
    }

    @Test
    fun sizeIsSet() {
        val message = SMFBuilder().putFloatCoordinates(2.5f, 15.6f).build()
        assertEquals(message.size, message[SMF.BYTES_SIZE_INDEX].toInt())
    }

    @Test
    fun stringOverflow() {
        val message = SMFBuilder().putString("test".repeat(100)).build()
        assertEquals(SMF.MAX_MESSAGE_SIZE, message.size)
    }

    @Test()
    fun argsTypeIsSet_noArgs() {
        val message = SMFBuilder().withNoArgs().build()
        assertEquals(SMF.ARGS_TYPE_NO_ARGS, message[SMF.BYTES_ARGS_TYPE_INDEX])
    }

    @Test()
    fun argsTypeIsSet_int() {
        val message = SMFBuilder().putInt(10).build()
        assertEquals(SMF.ARGS_TYPE_INT, message[SMF.BYTES_ARGS_TYPE_INDEX])
    }

    @Test()
    fun argsTypeIsSet_float() {
        val message = SMFBuilder().putFloat(10.5f).build()
        assertEquals(SMF.ARGS_TYPE_FLOAT, message[SMF.BYTES_ARGS_TYPE_INDEX])
    }

    @Test()
    fun argsTypeIsSet_string() {
        val message = SMFBuilder().putString("test").build()
        assertEquals(SMF.ARGS_TYPE_STRING, message[SMF.BYTES_ARGS_TYPE_INDEX])
    }

    @Test()
    fun argsTypeIsSet_intCoordinates() {
        val message = SMFBuilder().putIntCoordinates(25, 156).build()
        assertEquals(SMF.ARGS_TYPE_INT_COORDINATES, message[SMF.BYTES_ARGS_TYPE_INDEX])
    }

    @Test()
    fun argsTypeIsSet_floatCoordinates() {
        val message = SMFBuilder().putFloatCoordinates(2.5f, 15.6f).build()
        assertEquals(SMF.ARGS_TYPE_FLOAT_COORDINATES, message[SMF.BYTES_ARGS_TYPE_INDEX])
    }
}