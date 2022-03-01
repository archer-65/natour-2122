package com.unina.natourkt.common

/**
 * Generic DataState class, this wrapper class wraps our entire network/database response
 * so we can have states based on the response's state (successful, loading, failed).
 *
 * Constructor is made of
 * - Generic [data] object
 * - A [CustomMessage] object
 */
sealed class DataState<T>(
    /**
     * This attribute serves as data container for [T] class
     */
    val data: T? = null,
    /**
     * This attribute serves as error container for [CustomMessage]
     */
    val error: CustomMessage = CustomMessage.SomethingWentWrong
) {

    /**
     * A `Success` is a [DataState] that holds a generic data object
     */
    class Success<T>(data: T) : DataState<T>(data)

    /**
     * This is a [DataState] that represents a loading state
     */
    class Loading<T> : DataState<T>()

    /**
     * An `Error` is a [DataState] that holds [CustomMessage] object
     */
    class Error<T>(customMessage: CustomMessage) : DataState<T>(error = customMessage)


    /**
     * The `CustomMessage` class is a sealed class that contains a list of all the possible custom
     * error messages that can be returned from the APIs
     */
    sealed class CustomMessage {
        object UserNotFound : CustomMessage()
        object UserNotConfirmed : CustomMessage()
        object InvalidPassword : CustomMessage()
        object InvalidCredentials : CustomMessage()
        object UsernameExists : CustomMessage()
        object AliasExists : CustomMessage()
        object InvalidParameter : CustomMessage()
        object CodeDelivery : CustomMessage()
        object CodeMismatch : CustomMessage()
        object CodeExpired : CustomMessage()
        object AuthGeneric : CustomMessage()
        object DataCorrupted : CustomMessage()
        object NetworkError : CustomMessage()
        object Timeout : CustomMessage()
        object Unauthorized : CustomMessage()
        object InternalServerError : CustomMessage()
        object BadRequest : CustomMessage()
        object NotFound : CustomMessage()
        object NotAcceptable : CustomMessage()
        object ServiceUnavailable : CustomMessage()
        object Forbidden : CustomMessage()
        object Conflict : CustomMessage()
        object SomethingWentWrong : CustomMessage()
    }
}