package com.fravega.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * Date and time related utilities.
 */
class DateTimeUtils {

    fun getCurrentDate_Less1Day_DateFrom(format: String): String =
        LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern(format))

    fun getCurrentDate_ByFormat(format: String): String =
        LocalDate.now().format(DateTimeFormatter.ofPattern(format))

    fun getRandomDateInPast(yearsBefore: Int): String {
        val date = LocalDate.now().minusYears(yearsBefore.toLong())
            .withMonth(Random.nextInt(1, 13))
            .withDayOfMonth(Random.nextInt(1, 28))
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun getRandomDateInTheFuture(yearsAfter: Int): String {
        val date = LocalDate.now().plusYears(yearsAfter.toLong())
            .withMonth(Random.nextInt(1, 13))
            .withDayOfMonth(Random.nextInt(1, 28))
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }
}