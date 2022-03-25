package com.unina.natourkt.core.analytics

import java.util.concurrent.TimeUnit

/**
 * Future usage, could be useful to analyze the time between clicks on a content
 */
object AnalyticsUtil {
    fun getTimeDurationInSecBetweenNow(startTimeMillis: Long) =
        TimeUnit.SECONDS.convert(
            System.currentTimeMillis() - startTimeMillis,
            TimeUnit.MILLISECONDS
        )
}