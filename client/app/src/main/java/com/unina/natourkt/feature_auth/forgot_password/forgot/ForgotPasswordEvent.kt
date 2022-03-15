package com.unina.natourkt.feature_auth.forgot_password.forgot

import com.unina.natourkt.feature_auth.registration.signup.RegistrationEvent

sealed class ForgotPasswordEvent {
    data class EnteredUsername(val username: String) : ForgotPasswordEvent()
    object Reset : ForgotPasswordEvent()
}