package com.unina.natourkt.core.analytics

/**
 * All kind of AppEvents, each one extending [AnalyticsEvent]
 */
object AppEvents {
    object FirstLaunch : AnalyticsEvent(AnalyticsConstants.Events.FirstLaunch.EVENT)
    object SessionStart : AnalyticsEvent(AnalyticsConstants.Events.SessionStart.EVENT)

    class SendClicked(
        timeBetweenClicks: Long
    ) : AnalyticsEvent(
        AnalyticsConstants.Events.SendClicked.EVENT,
        mapOf(
            AnalyticsConstants.Events.SendClicked.Params.TIME_BETWEEN_CLICKS to timeBetweenClicks
        )
    )

    class Navigated(
        from: NavigationSource,
        to: NavigationSource,
    ) : AnalyticsEvent(
        AnalyticsConstants.Events.Navigated.EVENT,
        mapOf(
            AnalyticsConstants.Events.Navigated.Params.FROM to from.toConstant(),
            AnalyticsConstants.Events.Navigated.Params.TO to to.toConstant()
        ),
        listOf(AnalyticsProvider.ANALYTICS_FIREBASE)
    ) {
        enum class NavigationSource {
            // Main screens
            HOME,
            ROUTES,
            PROFILE,
            CHATS,
            ADMIN,

            // Admin feature
            REPORT_DETAIL,

            // Authentication feature
            LOGIN,
            FORGOT_PASSWORD,
            SIGNUP,
            LOGOUT,

            // Chat feature
            CHAT,
            USER_SEARCH,

            // Compilation feature
            COMPILATION_DETAIL,
            CREATE_COMPILATION,
            DELETE_COMPILATION,
            SAVE_TO_COMPILATION,
            REMOVING_FROM_COMPILATION,

            // Post feature
            CREATE_POST,
            DELETE_POST,
            POST_DETAIL,
            REPORT_POST,

            // Route feature
            CREATE_ROUTE,
            DELETE_ROUTE,
            RATE_ROUTE,
            REPORT_ROUTE,
            ROUTE_DETAIL,
            ROUTE_SEARCH,
            UPDATE_ROUTE,

            // Profile feature
            PROFILE_POSTS,
            PROFILE_ROUTES,
            PROFILE_COMPILATIONS;

            fun toConstant(): String =
                when (this) {
                    HOME -> AnalyticsConstants.Events.Navigated.NavigationSources.HOME
                    ROUTES -> AnalyticsConstants.Events.Navigated.NavigationSources.ROUTES
                    PROFILE -> AnalyticsConstants.Events.Navigated.NavigationSources.PROFILE
                    CHATS -> AnalyticsConstants.Events.Navigated.NavigationSources.CHATS
                    ADMIN -> AnalyticsConstants.Events.Navigated.NavigationSources.ADMIN
                    REPORT_DETAIL -> AnalyticsConstants.Events.Navigated.NavigationSources.REPORT_DETAIL
                    LOGIN -> AnalyticsConstants.Events.Navigated.NavigationSources.LOGIN
                    FORGOT_PASSWORD -> AnalyticsConstants.Events.Navigated.NavigationSources.FORGOT_PASSWORD
                    SIGNUP -> AnalyticsConstants.Events.Navigated.NavigationSources.SIGNUP
                    LOGOUT -> AnalyticsConstants.Events.Navigated.NavigationSources.LOGOUT
                    CHAT -> AnalyticsConstants.Events.Navigated.NavigationSources.CHAT
                    USER_SEARCH -> AnalyticsConstants.Events.Navigated.NavigationSources.USER_SEARCH
                    COMPILATION_DETAIL -> AnalyticsConstants.Events.Navigated.NavigationSources.COMPILATION_DETAIL
                    CREATE_COMPILATION -> AnalyticsConstants.Events.Navigated.NavigationSources.CREATE_COMPILATION
                    DELETE_COMPILATION -> AnalyticsConstants.Events.Navigated.NavigationSources.DELETE_COMPILATION
                    SAVE_TO_COMPILATION -> AnalyticsConstants.Events.Navigated.NavigationSources.SAVE_TO_COMPILATION
                    REMOVING_FROM_COMPILATION -> AnalyticsConstants.Events.Navigated.NavigationSources.REMOVING_FROM_COMPILATION
                    CREATE_POST -> AnalyticsConstants.Events.Navigated.NavigationSources.CREATE_POST
                    DELETE_POST -> AnalyticsConstants.Events.Navigated.NavigationSources.DELETE_POST
                    POST_DETAIL -> AnalyticsConstants.Events.Navigated.NavigationSources.POST_DETAIL
                    REPORT_POST -> AnalyticsConstants.Events.Navigated.NavigationSources.REPORT_POST
                    CREATE_ROUTE -> AnalyticsConstants.Events.Navigated.NavigationSources.CREATE_ROUTE
                    DELETE_ROUTE -> AnalyticsConstants.Events.Navigated.NavigationSources.DELETE_ROUTE
                    RATE_ROUTE -> AnalyticsConstants.Events.Navigated.NavigationSources.RATE_ROUTE
                    REPORT_ROUTE -> AnalyticsConstants.Events.Navigated.NavigationSources.REPORT_ROUTE
                    ROUTE_DETAIL -> AnalyticsConstants.Events.Navigated.NavigationSources.ROUTE_DETAIL
                    ROUTE_SEARCH -> AnalyticsConstants.Events.Navigated.NavigationSources.ROUTE_SEARCH
                    UPDATE_ROUTE -> AnalyticsConstants.Events.Navigated.NavigationSources.UPDATE_ROUTE
                    PROFILE_POSTS -> AnalyticsConstants.Events.Navigated.NavigationSources.PROFILE_POSTS
                    PROFILE_ROUTES -> AnalyticsConstants.Events.Navigated.NavigationSources.PROFILE_ROUTES
                    PROFILE_COMPILATIONS -> AnalyticsConstants.Events.Navigated.NavigationSources.PROFILE_COMPILATIONS
                }
        }
    }
}