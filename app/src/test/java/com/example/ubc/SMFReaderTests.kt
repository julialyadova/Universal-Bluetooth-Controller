package com.example.ubc

import com.example.ubc.messageformats.smf.SMFBuilder
import com.example.ubc.messageformats.smf.SMFReader
import org.junit.Assert.*
import org.junit.Test

class SMFReaderTests {
    @Test
    fun noArgs() {
        val message = SMFBuilder().putCommand("test").withNoArgs().build()
        var invoked = false
        SMFReader().withMessage(message).whenCommand("test").doIfNoArgs {
            invoked = true
        }
        assertTrue(invoked)
    }

    @Test
    fun intArg() {
        val message = SMFBuilder().putCommand("test").putInt(10).build()
        var value = 0
        SMFReader().withMessage(message).whenCommand("test").doIfIntArg {
            value = it
        }
        assertEquals(10, value)
    }

    @Test
    fun stringArg() {
        val message = SMFBuilder().putCommand("test").putString("test").build()
        var str = ""
        SMFReader().withMessage(message).whenCommand("test").doIfStringArg {
            str = it
        }
        assertEquals("test", str)
    }

    @Test
    fun coordinatesArg() {
        val message = SMFBuilder().putCommand("test").putIntCoordinates(20, 49).build()
        var x = 0
        var y = 0
        SMFReader().withMessage(message).whenCommand("test").doIfIntCoordinatesArgs { x0, y0 ->
            x = x0
            y = y0
        }
        assertEquals(20, x)
        assertEquals(49, y)
    }

    @Test
    fun doNothingOnInvalidArgs() {
        val message = SMFBuilder().putCommand("test").putIntCoordinates(20, 49).build()
        var invoked = false
        SMFReader().withMessage(message).whenCommand("test").doIfIntArg {
            invoked = true
        }
        assertFalse(invoked)
    }
}