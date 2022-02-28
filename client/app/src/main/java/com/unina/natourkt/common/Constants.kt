package com.unina.natourkt.common

/**
 * This file contains all the possible constants used for
 * non-localized strings and tags
 */
object Constants {

    // EMULATOR
    //const val BASE_URL = "http://10.0.2.2:8080/"
    // LOCAL IP
    const val BASE_URL = "http://192.168.1.47:8080/"

    const val MAPS_URL = "https://maps.googleapis.com/"

    // String used for Auth Providers
    const val GOOGLE = "GOOGLE"
    const val FACEBOOK = "FACEBOOK"

    // TAGS
    const val AMPLIFY = "Amplify Status"

    const val LOGIN_STATE = "Login Process"
    const val REGISTRATION_STATE = "Sign Up Process"
    const val PASSWORD_RESET = "Password Reset Process"

    const val DATASTORE_STATE = "Datastore Status"

    const val POST_MODEL = "Post Request Status"
    const val ROUTE_MODEL = "Route Request Status"
    const val COMPILATION_MODEL = "Compilation Request Status"


    // DATASTORE NAME(S)
    const val PREFERENCES = "DATASTORE_PREF"

    // OTHER
    const val PASSWORD_LENGTH = 8

    // GRID LAYOUT
    const val COLUMN_COUNT = 4
    const val COLUMN_SPACING = 5
}