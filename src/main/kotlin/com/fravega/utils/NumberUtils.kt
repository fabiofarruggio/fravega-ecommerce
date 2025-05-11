package com.fravega.utils

import java.util.Random

/**
 * Number generation and formatting utilities.
 */
class NumberUtils {
    fun getRandomInt8DigitNumber(): Int = Random().nextInt(100_000_000)
    fun roundOffDecimal(number: Float): Float = "%.2f".format(number).toFloat()
}