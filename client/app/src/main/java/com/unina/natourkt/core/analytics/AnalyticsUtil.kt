package com.unina.natourkt.core.analytics

import java.util.concurrent.TimeUnit

object AnalyticsUtil {
    fun getTimeDurationInSecBetweenNow(startTimeMillis: Long) =
        TimeUnit.SECONDS.convert(
            System.currentTimeMillis() - startTimeMillis,
            TimeUnit.MILLISECONDS
        )
}