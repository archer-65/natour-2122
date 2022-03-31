package com.unina.natourkt.core.util

/**
 * This file contains all the possible constants used for
 * non-localized strings and tags
 */
object Constants {

    /**
     * LOCAL TEST SECTION
     */
    // Emulator
    // const val BASE_URL = "http://10.0.2.2:8080/"
    // Local IP
    // const val BASE_URL = "http://192.168.1.47:8080/"
    //const val BASE_WS = "ws://192.168.1.47:8080/ws"


    /**
     * APP
     */
    // String used for Auth Providers
    const val GOOGLE = "GOOGLE"
    const val FACEBOOK = "FACEBOOK"

    // Log tags
    const val NETWORK_REQUEST = "Network Request Status"
    const val NETWORK_ERROR = "Network Error"
    const val SERVER_ERROR = "Server unreachable"

    const val AMPLIFY = "Amplify Status"
    const val MAPS = "Maps request"
    const val DATASTORE_STATE = "Datastore Status"
    const val STORAGE = "Storage request"

    const val LOGIN_STATE = "Login process"
    const val REGISTRATION_STATE = "Sign Up process"
    const val PASSWORD_RESET = "Password reset process"

    const val USER_MODEL = "User request status"
    const val POST_MODEL = "Post request status"
    const val CHAT_MODEL = "Chat request status"
    const val ROUTE_MODEL = "Route request status"
    const val COMPILATION_MODEL = "Compilation request status"
    const val RATING_MODEL = "Rating route request status"
    const val REPORT_MODEL = "Report route request status"

    // DATASTORE NAME(S)
    const val PREFERENCES = "DATASTORE_PREF"

    // Form validation
    const val PASSWORD_LENGTH = 8
    const val CODE_LENGTH = 6

    // Grid Layout
    const val COLUMN_COUNT = 4
    const val COLUMN_SPACING = 5

    // Photos
    const val MAX_PHOTO = 5
}