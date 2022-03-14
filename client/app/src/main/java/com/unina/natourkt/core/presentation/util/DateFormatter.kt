package com.unina.natourkt.core.presentation.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun LocalDateTime.formatFull(): String {
    val DATE_FORMATTER: DateTimeFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.getDefault())

    return this.format(DATE_FORMATTER)
}