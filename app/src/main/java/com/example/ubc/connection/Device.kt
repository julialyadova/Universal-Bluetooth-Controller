package com.example.ubc.connection

class Device (
        val name: String?,
        val address: String,
        val info: String? = null
) {

    override fun equals(other: Any?): Boolean {
        return if (other is Device)
            other.address == this.address
        else
            false
    }
}