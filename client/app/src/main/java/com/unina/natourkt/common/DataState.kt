package com.unina.natourkt.common

/**
 * Generic wrapper class, usually returned by UseCase classes
 */
sealed class DataState<T>(
    val data: T? = null,
    val error: CustomMessages = CustomMessages.SomethingWentWrong("Something went wrong")
) {
    class Success<T>(data: T) : DataState<T>(data)
    class Loading<T> : DataState<T>()
    class Error<T>(customMessages: CustomMessages) : DataState<T>(error = customMessages)

    sealed class CustomMessages(val message: String = "") {
        object UserNotFound : CustomMessages()
        object UserNotConfirmed : CustomMessages()
        object InvalidPassword : CustomMessages()
        object InvalidCredentials: CustomMessages()
        object UsernameExists : CustomMessages()
        object AliasExists : CustomMessages()
        object InvalidParameter : CustomMessages()
        object CodeDelivery : CustomMessages()
        object CodeMismatch : CustomMessages()
        object CodeExpired : CustomMessages()
        object AuthGeneric : CustomMessages()
        object DataCorrupted : CustomMessages()
        object NetworkError : CustomMessages()
        object Timeout : CustomMessages()
        object Unauthorized : CustomMessages()
        object InternalServerError : CustomMessages()
        object BadRequest : CustomMessages()
        object NotFound : CustomMessages()
        object NotAcceptable : CustomMessages()
        object ServiceUnavailable : CustomMessages()
        object Forbidden : CustomMessages()
        object Conflict : CustomMessages()
        data class SomethingWentWrong(val customMessage: String) : CustomMessages(customMessage)
    }
}