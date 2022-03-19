package com.unina.natourkt.core.presentation.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun LocalDateTime.formatFull(): String {
    val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault())

    return this.format(DATE_FORMATTER)
}

fun LocalDateTime.format(): String {
    val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.getDefault())

    return this.format(DATE_FORMATTER)
}

fun LocalDate.format(): String {
    val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.getDefault())

    return this.format(DATE_FORMATTER)
}

fun LocalTime.format(): String {
    val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.getDefault())

    return this.format(DATE_FORMATTER)
}