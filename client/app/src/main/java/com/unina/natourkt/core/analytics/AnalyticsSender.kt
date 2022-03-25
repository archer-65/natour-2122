package com.unina.natourkt.core.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

/**
 * This function serves as channel of communication for supported analytics providers
 */
class AnalyticsSender @Inject constructor(
    private val analyticsFirebase: FirebaseAnalytics
) {

    /**
     * This function send events to all the analytics providers supported, specifically, these
     * providers contained in [AnalyticsEvent.providers]
     */
    fun sendEvent(event: AnalyticsEvent) {
        if (event.providers.contains(AnalyticsProvider.ANALYTICS_FIREBASE)) {
            analyticsFirebase.logEvent(event.eventName, event.params.toBundle())
        }
    }
}