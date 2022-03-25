package com.unina.natourkt.core.presentation.util

import android.text.InputFilter
import android.text.Spanned

class DurationFilter(private val minValue: Int, private val maxValue: Int) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(minValue, maxValue, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(min: Int, max: Int, input: Int): Boolean {
        return if (max > min) input in min..max else input in max..min
    }
}