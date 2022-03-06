package com.unina.natourkt.common

/**
 * Generic DataState class, this wrapper class wraps our entire network/database response
 * so we can have states based on the response's state (successful, loading, failed).
 *
 * Constructor is made of
 * - Generic [data] object
 * - A [Cause] object
 */
sealed class DataState<T>(
    /**
     * This attribute serves as data container for [T] class
     */
    val data: T? = null,
    /**
     * This attribute serves as error container for [Cause]
     */
    val error: Cause = Cause.SomethingWentWrong
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
     * An `Error` is a [DataState] that holds [Cause] object
     */
    class Error<T>(cause: Cause) : DataState<T>(error = cause)


    /**
     * The `CustomMessage` class is a sealed class that contains a list of all the possible custom
     * error messages that can be returned from the APIs
     */
    sealed class Cause {
        object UserNotFound : Cause()
        object UserNotConfirmed : Cause()
        object InvalidPassword : Cause()
        object InvalidCredentials : Cause()
        object UsernameExists : Cause()
        object AliasExists : Cause()
        object InvalidParameter : Cause()
        object CodeDelivery : Cause()
        object CodeMismatch : Cause()
        object CodeExpired : Cause()
        object AuthGeneric : Cause()
        object DataCorrupted : Cause()
        object NetworkError : Cause()
        object Timeout : Cause()
        object Unauthorized : Cause()
        object InternalServerError : Cause()
        object BadRequest : Cause()
        object NotFound : Cause()
        object NotAcceptable : Cause()
        object ServiceUnavailable : Cause()
        object Forbidden : Cause()
        object Conflict : Cause()
        object SomethingWentWrong : Cause()
    }
}