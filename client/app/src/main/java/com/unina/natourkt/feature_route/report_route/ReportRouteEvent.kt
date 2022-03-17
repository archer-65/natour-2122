package com.unina.natourkt.feature_route.report_route

sealed class ReportRouteEvent {
    // GENERAL
    object Upload : ReportRouteEvent()

    // INFO
    data class EnteredTitle(val title: String) : ReportRouteEvent()
    data class EnteredDescription(val description: String) : ReportRouteEvent()
}