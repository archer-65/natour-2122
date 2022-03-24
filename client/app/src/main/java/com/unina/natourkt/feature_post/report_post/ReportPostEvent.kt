package com.unina.natourkt.feature_post.report_post

sealed class ReportPostEvent {
    object SendReport : ReportPostEvent()
}