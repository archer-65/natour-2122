package com.unina.natourkt.common

import androidx.datastore.core.CorruptionException
import com.amplifyframework.auth.AuthException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException

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
    fun handleException(throwable: Throwable): DataState.CustomMessage {

        return when (throwable) {
            // AuthException (Amplify)
            is AuthException -> getAuthError(throwable)

            // DataStore
            is CorruptionException -> DataState.CustomMessage.DataCorrupted

            // Network related
            is NetworkConnectionInterceptor.NoConnectivityException -> DataState.CustomMessage.NetworkError
            is IOException -> DataState.CustomMessage.NetworkError
            is HttpException -> getErrorType(throwable.code())

            // Generic
            else -> DataState.CustomMessage.SomethingWentWrong
        }
    }
}


/**
 * This function returns the error type based on the error code
 */
private fun getErrorType(code: Int): DataState.CustomMessage {
    return when (code) {
        ErrorCodes.SocketTimeOut.code -> DataState.CustomMessage.Timeout
        ErrorCodes.UnAuthorized.code -> DataState.CustomMessage.Unauthorized
        ErrorCodes.InternalServerError.code -> DataState.CustomMessage.InternalServerError
        ErrorCodes.BadRequest.code -> DataState.CustomMessage.BadRequest
        ErrorCodes.Conflict.code -> DataState.CustomMessage.Conflict
        ErrorCodes.NotFound.code -> DataState.CustomMessage.NotFound
        ErrorCodes.NotAcceptable.code -> DataState.CustomMessage.NotAcceptable
        ErrorCodes.ServiceUnavailable.code -> DataState.CustomMessage.ServiceUnavailable
        ErrorCodes.Forbidden.code -> DataState.CustomMessage.Forbidden
        else -> DataState.CustomMessage.SomethingWentWrong
    }
}

/**
 * This function returns a DataState.CustomMessage based on the AuthException thrown
 */
private fun getAuthError(throwable: AuthException): DataState.CustomMessage {
    return when (throwable) {
        is AuthException.UserNotFoundException -> DataState.CustomMessage.UserNotFound
        is AuthException.UserNotConfirmedException -> DataState.CustomMessage.UserNotConfirmed
        is AuthException.InvalidPasswordException -> DataState.CustomMessage.InvalidPassword
        is AuthException.NotAuthorizedException -> DataState.CustomMessage.InvalidCredentials
        is AuthException.UsernameExistsException -> DataState.CustomMessage.UsernameExists
        is AuthException.AliasExistsException -> DataState.CustomMessage.AliasExists
        is AuthException.InvalidParameterException -> DataState.CustomMessage.InvalidParameter
        is AuthException.CodeDeliveryFailureException -> DataState.CustomMessage.CodeDelivery
        is AuthException.CodeMismatchException -> DataState.CustomMessage.CodeMismatch
        is AuthException.CodeExpiredException -> DataState.CustomMessage.CodeExpired
        else -> DataState.CustomMessage.AuthGeneric
    }
}