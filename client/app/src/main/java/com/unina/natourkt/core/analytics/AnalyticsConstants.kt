package com.unina.natourkt.core.analytics

/**
 * This object contains [Events] representing certain events in the app, each event is represented
 * by another object which contains constants for the event name or the event name and various parameters
 */
object AnalyticsConstants {
    object Events {
        object FirstLaunch {
            const val EVENT = "FIRST_LAUNCH_NATOUR"
        }

        object SessionStart {
            const val EVENT = "SESSION_STARTED"
        }

        object Navigated {
            const val EVENT = "navigated"

            object Params {
                const val FROM = "from"
                const val TO = "to"
            }

            object NavigationSources {
                // Main screens
                const val HOME = "HOME_WITH_RECENT_POSTS_SCREEN"
                const val ROUTES = "ROUTES_SCREEN"
                const val PROFILE = "PERSONAL_PROFILE_SCREEN"
                const val CHATS = "PERSONAL_CHATS_SCREEN"
                const val ADMIN = "ADMIN_PANEL_SCREEN"

                // Admin feature
                const val REPORT_DETAIL = "REPORT_MANAGEMENT"

                // Authentication feature
                const val LOGIN = "LOGIN_FLOW"
                const val FORGOT_PASSWORD = "FORGOT_PASSWORD_FLOW"
                const val SIGNUP = "SIGNUP_FLOW"
                const val LOGOUT = "LOGGING_OUT"

                // Chat feature
                const val CHAT = "CHAT_WITH_USER"
                const val USER_SEARCH = "SEARCH_USER_TO_CHAT"

                // Compilation feature
                const val COMPILATION_DETAIL = "COMPILATION_DETAIL_SCREEN"
                const val CREATE_COMPILATION = "CREATING_COMPILATION"
                const val DELETE_COMPILATION = "DELETING_COMPILATION_DIALOG"
                const val SAVE_TO_COMPILATION = "SAVING_ROUTE_TO_COMPILATION"
                const val REMOVING_FROM_COMPILATION = "REMOVING_ROUTE_FROM_COMPILATION"

                // Post feature
                const val CREATE_POST = "CREATING_POST"
                const val DELETE_POST = "DELETING_POST_DIALOG"
                const val POST_DETAIL = "POST_DETAIL_SCREEN"
                const val REPORT_POST = "REPORTING_POST_DIALOG"

                // Route feature
                const val CREATE_ROUTE = "CREATING_ROUTE"
                const val DELETE_ROUTE = "DELETING_ROUTE_DIALOG"
                const val RATE_ROUTE = "RATING_ROUTE"
                const val REPORT_ROUTE = "REPORTING_ROUTE"
                const val ROUTE_DETAIL = "ROUTE_DETAILS_SCREEN"
                const val ROUTE_SEARCH = "SEARCH_ROUTE_WITH_FILTERS"
                const val UPDATE_ROUTE = "UPDATING_ROUTE_DESCRIPTION"

                // Profile feature
                const val PROFILE_POSTS = "PERSONAL_POSTS_TAB"
                const val PROFILE_ROUTES = "PERSONAL_ROUTES_TAB"
                const val PROFILE_COMPILATIONS = "PERSONAL_COMPILATIONS_TAB"
            }
        }

        object SendClicked {
            const val EVENT = "SEND_CLICKED"

            object Params {
                const val TIME_BETWEEN_CLICKS = "TIME_BETWEEN_CLICKS"
            }
        }

        object Actions {
            // Authentication feature
            const val LOGGED_IN = "user_logged_in"
            const val LOGGED_OUT = "user_logged_out"
            const val SIGN_UP_SEND = "user_sent_sign_up_form"
            const val SIGNED_UP = "user_signed_up"
            const val FORGOT_PASSWORD = "user_wants_to_recover_password"
            const val RESET_PASSWORD = "user_reset_password"

            // Admin feature
            const val CLICK_REPORT = "admin_managing_report"
            const val DELETE_REPORT = "admin_deleted_report"
            const val UPDATE_ROUTE = "admin_updating_route"

            // Chat feature
            const val CLICK_CHAT = "user_is_chatting"
            const val SEND_MESSAGE = "user_sent_private_message"
            const val SEARCHING_USER = "user_searching_other_users"

            // Compilation feature
            const val CLICK_COMPILATION = "user_watching_one_compilation"
            const val COMPILATION_CREATED = "compilation_created"
            const val COMPILATION_DELETED = "compilation_deleted"
            const val SAVE_INTO_COMPILATION = "route_added_to_compilation"
            const val REMOVE_FROM_COMPILATION = "route_removed_from_compilation"

            // Feature post
            const val POST_CREATED = "post_created"
            const val POST_DELETED = "post_deleted"
            const val CLICK_POST = "user_watching_post"
            const val POST_REPORTED = "post_reported"

            // Feature route
            const val ROUTE_CREATED = "route_created"
            const val MAP_CLEANED = "user_removed_map_markers"
            const val MARKER_ADD = "user_added_marker"
            const val SEARCHING_PLACE = "user_searching_place"
            const val SELECTING_GPX = "user_selecting_gpx"
            const val ROUTE_DELETED = "route_deleted"
            const val ROUTE_RATED = "route_rated"
            const val ROUTE_REPORTED = "route_reported"
            const val CLICK_ROUTE = "user_watching_route"
            const val SEARCHING_ROUTE = "user_filtering_route"

            // Profile feature
            const val PROFILE_PHOTO_UPDATED = "user_changed_profile_photo"
            const val CHANGE_THEME = "user_changed_app_theme"
        }
    }
}