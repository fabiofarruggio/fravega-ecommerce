package com.fravega.utils

/**
 * Utility to generate randomized test data.
 */
class DataUtils {
    fun getRandomEmail(): String = "mail-${NumberUtils().getRandomInt8DigitNumber()}@fravega.com"
}