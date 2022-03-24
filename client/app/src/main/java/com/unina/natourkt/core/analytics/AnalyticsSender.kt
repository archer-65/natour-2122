package com.unina.natourkt.core.analytics

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class AnalyticsSender @Inject constructor(
    private val analyticsFirebase: FirebaseAnalytics
) {

    fun sendEvent(event: AnalyticsEvent) {
        if (event.providers.contains(AnalyticsProvider.ANALYTICS_FIREBASE)) {
            analyticsFirebase.logEvent(event.eventName, event.params.toBundle())
        }
    }
}