package com.unina.natourkt.core.domain.use_case.analytics

import com.unina.natourkt.core.analytics.AnalyticsEvent
import com.unina.natourkt.core.analytics.AnalyticsSender
import javax.inject.Inject

class ActionAnalyticsUseCase @Inject constructor(
    private val actionSender: AnalyticsSender
) {

    fun sendEvent(event: AnalyticsEvent) {
        actionSender.sendEvent(event)
    }
}