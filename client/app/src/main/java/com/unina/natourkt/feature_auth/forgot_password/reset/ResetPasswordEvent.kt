package com.unina.natourkt.feature_auth.forgot_password.reset

sealed class ResetPasswordEvent {
    data class EnteredCode(val code: String) : ResetPasswordEvent()
    data class EnteredPassword(val password: String) : ResetPasswordEvent()
    data class EnteredConfirmPassword(val confirmPassword: String) : ResetPasswordEvent()
    object Reset : ResetPasswordEvent()
}