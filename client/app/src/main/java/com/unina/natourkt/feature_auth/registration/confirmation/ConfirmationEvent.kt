package com.unina.natourkt.feature_auth.registration.confirmation

sealed class ConfirmationEvent {
    data class EnteredCode(val code: String) : ConfirmationEvent()
    data class Confirm(val user: String) : ConfirmationEvent()
    data class Resend(val user: String) : ConfirmationEvent()
}