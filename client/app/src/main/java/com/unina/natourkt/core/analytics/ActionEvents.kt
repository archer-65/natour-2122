package com.unina.natourkt.core.analytics

object ActionEvents {
    object LoggedIn : AnalyticsEvent(AnalyticsConstants.Events.Actions.LOGGED_IN)
    object LoggedOut : AnalyticsEvent(AnalyticsConstants.Events.Actions.LOGGED_OUT)
    object SignUpSubmit : AnalyticsEvent(AnalyticsConstants.Events.Actions.SIGN_UP_SEND)
    object SignedUp : AnalyticsEvent(AnalyticsConstants.Events.Actions.SIGNED_UP)
    object ForgotPassword : AnalyticsEvent(AnalyticsConstants.Events.Actions.FORGOT_PASSWORD)
    object ResetPassword : AnalyticsEvent(AnalyticsConstants.Events.Actions.RESET_PASSWORD)

    object ClickReport : AnalyticsEvent(AnalyticsConstants.Events.Actions.CLICK_REPORT)
    object UpdateRoute : AnalyticsEvent(AnalyticsConstants.Events.Actions.UPDATE_ROUTE)

    object ClickChat : AnalyticsEvent(AnalyticsConstants.Events.Actions.CLICK_CHAT)
    object SendMessage : AnalyticsEvent(AnalyticsConstants.Events.Actions.SEND_MESSAGE)
    object SearchUser : AnalyticsEvent(AnalyticsConstants.Events.Actions.SEARCHING_USER)

    object ClickCompilation : AnalyticsEvent(AnalyticsConstants.Events.Actions.CLICK_COMPILATION)
    object CreateCompilation : AnalyticsEvent(AnalyticsConstants.Events.Actions.COMPILATION_CREATED)
    object DeleteCompilation : AnalyticsEvent(AnalyticsConstants.Events.Actions.COMPILATION_DELETED)
    object SaveIntoCompilation: AnalyticsEvent(AnalyticsConstants.Events.Actions.SAVE_INTO_COMPILATION)
    object RemoveFromCompilation: AnalyticsEvent(AnalyticsConstants.Events.Actions.REMOVE_FROM_COMPILATION)

    object CreatePost : AnalyticsEvent(AnalyticsConstants.Events.Actions.POST_CREATED)
    object DeletePost : AnalyticsEvent(AnalyticsConstants.Events.Actions.POST_DELETED)
    object ClickPost : AnalyticsEvent(AnalyticsConstants.Events.Actions.CLICK_POST)
    object ReportPost: AnalyticsEvent(AnalyticsConstants.Events.Actions.POST_REPORTED)

    object CreateRoute : AnalyticsEvent(AnalyticsConstants.Events.Actions.ROUTE_CREATED)
    object CleanMap : AnalyticsEvent(AnalyticsConstants.Events.Actions.MAP_CLEANED)
    object AddMarker : AnalyticsEvent(AnalyticsConstants.Events.Actions.MARKER_ADD)
    object SearchPlace: AnalyticsEvent(AnalyticsConstants.Events.Actions.SEARCHING_PLACE)
    object DeleteRoute: AnalyticsEvent(AnalyticsConstants.Events.Actions.ROUTE_DELETED)
    object RateRoute : AnalyticsEvent(AnalyticsConstants.Events.Actions.ROUTE_RATED)
    object ReportRoute : AnalyticsEvent(AnalyticsConstants.Events.Actions.ROUTE_REPORTED)
    object ClickRoute : AnalyticsEvent(AnalyticsConstants.Events.Actions.CLICK_ROUTE)
    object SearchRoute: AnalyticsEvent(AnalyticsConstants.Events.Actions.SEARCHING_ROUTE)

    object ProfilePhotoUpdate: AnalyticsEvent(AnalyticsConstants.Events.Actions.PROFILE_PHOTO_UPDATED)
    object ThemeChanged: AnalyticsEvent(AnalyticsConstants.Events.Actions.CHANGE_THEME)
}