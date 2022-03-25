package com.unina.natourkt.feature_auth.forgot_password.forgot

sealed class ForgotPasswordEvent {
    data class EnteredUsername(val username: String) : ForgotPasswordEvent()
    object Reset : ForgotPasswordEvent()
}