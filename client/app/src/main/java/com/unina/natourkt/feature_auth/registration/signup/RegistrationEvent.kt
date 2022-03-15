package com.unina.natourkt.feature_auth.registration.signup

sealed class RegistrationEvent {
    data class EnteredUsername(val username: String) : RegistrationEvent()
    data class EnteredEmail(val email: String) : RegistrationEvent()
    data class EnteredPassword(val password: String) : RegistrationEvent()
    data class EnteredConfirmPassword(val confirmPassword: String) : RegistrationEvent()
    object Registration : RegistrationEvent()
}