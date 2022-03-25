package com.unina.natourkt.core.analytics

/**
 * This abstract class represent the standard composition of an event and a list of supported providers
 * @see AnalyticsConstants
 */
abstract class AnalyticsEvent(
    // Name of the event
    val eventName: String,
    // Key values pair of param, where key is name of event
    val params: Map<String, Any>? = emptyMap(),
    // List of supported providers
    val providers: List<AnalyticsProvider> = listOf(
        AnalyticsProvider.ANALYTICS_FIREBASE
    )
)