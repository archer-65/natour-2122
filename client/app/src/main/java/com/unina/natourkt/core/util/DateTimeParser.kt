package com.unina.natourkt.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * CREDITS:
 * https://github.com/raywenderlich/adva-materials/blob/editions/1.0/22-app-analysis/projects/final/app/src/main/java/com/raywenderlich/android/petsave/core/utils/DateTimeUtils.kt
 */
object DateTimeParser {
    fun parseDateTime(dateTimeString: String): LocalDateTime = try {
        LocalDateTime.parse(dateTimeString)
    } catch (e: Exception) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        LocalDateTime.parse(dateTimeString, dateFormatter)
    }

    fun parseDate(dateString: String): LocalDate = try {
        LocalDate.parse(dateString)
    } catch (e: Exception) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        LocalDate.parse(dateString, dateFormatter)
    }
}