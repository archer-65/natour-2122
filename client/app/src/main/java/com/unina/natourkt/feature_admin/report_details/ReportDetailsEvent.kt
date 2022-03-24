package com.unina.natourkt.feature_admin.report_details

sealed class ReportDetailsEvent {
    object OnReportDelete : ReportDetailsEvent()
}