package com.unina.natourkt.common

import androidx.datastore.core.CorruptionException
import com.amplifyframework.auth.AuthException
import retrofit2.HttpException
import java.io.IOException

/**
 * Used for HTTP Exceptions, each error name with its own error code
 */
enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1),
    BadRequest(400),
    NotFound(404),
    Conflict(409),
    InternalServerError(500),
    Forbidden(403),
    NotAcceptable(406),
    ServiceUnavailable(503),
    UnAuthorized(401),
}

/**
 * ErrorHandler class, maps every kind of Exception to one CustomMessage type
 */
open class ErrorHandler {
    fun <T : Any> handleException(throwable: Throwable): DataState.CustomMessages {

        return when (throwable) {

            // AuthException (Amplify)
            is AuthException -> {

                when (throwable) {
                    is AuthException.UserNotFoundException -> DataState.CustomMessages.UserNotFound
                    is AuthException.UserNotConfirmedException -> DataState.CustomMessages.UserNotConfirmed
                    is AuthException.InvalidPasswordException -> DataState.CustomMessages.InvalidPassword
                    is AuthException.NotAuthorizedException -> DataState.CustomMessages.InvalidCredentials
                    is AuthException.UsernameExistsException -> DataState.CustomMessages.UsernameExists
                    is AuthException.AliasExistsException -> DataState.CustomMessages.AliasExists
                    is AuthException.InvalidParameterException -> DataState.CustomMessages.InvalidParameter
                    is AuthException.CodeDeliveryFailureException -> DataState.CustomMessages.CodeDelivery
                    is AuthException.CodeMismatchException -> DataState.CustomMessages.CodeMismatch
                    is AuthException.CodeExpiredException -> DataState.CustomMessages.CodeExpired
                    else -> DataState.CustomMessages.AuthGeneric
                }
            }

            // DataStore
            is CorruptionException -> DataState.CustomMessages.DataCorrupted

            // Network related
            is IOException -> DataState.CustomMessages.NetworkError
            is HttpException -> getErrorType(throwable.code())
            else -> DataState.CustomMessages.SomethingWentWrong("Unknown Error")
        }
    }
}

/**
 * [HttpException]
 */
private fun getErrorType(code: Int): DataState.CustomMessages {
    return when (code) {
        ErrorCodes.SocketTimeOut.code -> DataState.CustomMessages.Timeout
        ErrorCodes.UnAuthorized.code -> DataState.CustomMessages.Unauthorized
        ErrorCodes.InternalServerError.code -> DataState.CustomMessages.InternalServerError
        ErrorCodes.BadRequest.code -> DataState.CustomMessages.BadRequest
        ErrorCodes.Conflict.code -> DataState.CustomMessages.Conflict
        ErrorCodes.NotFound.code -> DataState.CustomMessages.NotFound
        ErrorCodes.NotAcceptable.code -> DataState.CustomMessages.NotAcceptable
        ErrorCodes.ServiceUnavailable.code -> DataState.CustomMessages.ServiceUnavailable
        ErrorCodes.Forbidden.code -> DataState.CustomMessages.Forbidden
        else -> DataState.CustomMessages.SomethingWentWrong("An error occurred with code $code")
    }
}