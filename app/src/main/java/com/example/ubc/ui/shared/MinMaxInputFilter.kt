package com.example.ubc.ui.shared

import android.text.InputFilter
import android.text.Spanned


class MinMaxInputFilter(
    private var min: Int,
    private var max: Int
) : InputFilter {

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (input in min..max) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }
}