package com.unina.natourkt.core.domain.use_case.analytics

import com.unina.natourkt.core.analytics.AnalyticsEvent
import com.unina.natourkt.core.analytics.AnalyticsSender
import javax.inject.Inject

/**
 * This UseCase is used globally to send events with Analytics Provider like
 * `Firebase Analytics` or `Amplitude`.
 * This implementation, with an injected [AnalyticsSender], is required to reach abstractness
 * and avoid vendor lock-in.
 */
class ActionAnalyticsUseCase @Inject constructor(
    private val actionSender: AnalyticsSender
) {

    /**
     * Function to send an event, takes only the event of [AnalyticsEvent] type
     */
    fun sendEvent(event: AnalyticsEvent) {
        actionSender.sendEvent(event)
    }
}