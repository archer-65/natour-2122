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
 * This class is responsible for handling all the exceptions that occur in the app
 */
open class ErrorHandler {

    companion object {
        fun handleException(throwable: Throwable): DataState.Cause {
            return when (throwable) {
                // AuthException (Amplify)
                is AuthException -> getAuthError(throwable)

                // DataStore
                is CorruptionException -> DataState.Cause.DataCorrupted

                // Network related
                is NetworkConnectionInterceptor.NoConnectivityException -> DataState.Cause.NetworkError
                is IOException -> DataState.Cause.NetworkError
                is HttpException -> getErrorType(throwable.code())

                // Generic
                else -> DataState.Cause.SomethingWentWrong
            }
        }
    }
}


/**
 * This function returns the error type based on the error code
 */
private fun getErrorType(code: Int): DataState.Cause {
    return when (code) {
        ErrorCodes.SocketTimeOut.code -> DataState.Cause.Timeout
        ErrorCodes.UnAuthorized.code -> DataState.Cause.Unauthorized
        ErrorCodes.InternalServerError.code -> DataState.Cause.InternalServerError
        ErrorCodes.BadRequest.code -> DataState.Cause.BadRequest
        ErrorCodes.Conflict.code -> DataState.Cause.Conflict
        ErrorCodes.NotFound.code -> DataState.Cause.NotFound
        ErrorCodes.NotAcceptable.code -> DataState.Cause.NotAcceptable
        ErrorCodes.ServiceUnavailable.code -> DataState.Cause.ServiceUnavailable
        ErrorCodes.Forbidden.code -> DataState.Cause.Forbidden
        else -> DataState.Cause.SomethingWentWrong
    }
}

/**
 * This function returns a DataState.CustomMessage based on the AuthException thrown
 */
private fun getAuthError(throwable: AuthException): DataState.Cause {
    return when (throwable) {
        is AuthException.UserNotFoundException -> DataState.Cause.UserNotFound
        is AuthException.UserNotConfirmedException -> DataState.Cause.UserNotConfirmed
        is AuthException.InvalidPasswordException -> DataState.Cause.InvalidPassword
        is AuthException.NotAuthorizedException -> DataState.Cause.InvalidCredentials
        is AuthException.UsernameExistsException -> DataState.Cause.UsernameExists
        is AuthException.AliasExistsException -> DataState.Cause.AliasExists
        is AuthException.InvalidParameterException -> DataState.Cause.InvalidParameter
        is AuthException.CodeDeliveryFailureException -> DataState.Cause.CodeDelivery
        is AuthException.CodeMismatchException -> DataState.Cause.CodeMismatch
        is AuthException.CodeExpiredException -> DataState.Cause.CodeExpired
        else -> DataState.Cause.AuthGeneric
    }
}