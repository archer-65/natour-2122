package com.unina.natourkt.core.data.util

import android.util.Log
import com.unina.natourkt.core.data.util.NetworkConstants.NETWORK_TIMEOUT
import com.unina.natourkt.core.util.Constants.NETWORK_ERROR
import com.unina.natourkt.core.util.Constants.SERVER_ERROR
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

/**
 * Reference 1: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 * Reference 2: https://github.com/mitchtabian/Clean-Notes/blob/ea8f6c95c57685aed42b3b1286aecb33cc2bbf77/app/src/main/java/com/codingwithmitch/cleannotes/business/data/util/RepositoryExtensions.kt
 *
 * NOTE: This class has been modified multiple times due to requirements!
 * This convenience method meant to be a shortcut for direct calls to retrofit interface methods.
 * To avoid boilerplate code everywhere, a dispatcher is injected as parameter, the second one is a value that
 * expresses the maximum request time, while the third parameter is
 * a lambda function taking every kind and number of parameters and returning any kind of type INCLUDED in the [DataState]
 * wrapper utility
 *
 * During the first step, in the `try body`, the function runs the given code, wrapping it in [DataState.Success],
 * however, if the [NETWORK_TIMEOUT] is exceeded [withTimeout] throws a [TimeoutCancellationException].
 *
 * There are three catch blocks: one for [TimeoutCancellationException],
 * another for [IOException] (in most of the cases this becomes `ConnectivityException`), but
 * the conversion based on the type of instance is made by [ErrorHandler].
 * The third one is a common [HttpException].
 */

suspend fun <T> retrofitSafeCall(
    dispatcher: CoroutineDispatcher,
    timeout: Long = NETWORK_TIMEOUT,
    apiCall: suspend () -> T,
): DataState<T> {
    return withContext(dispatcher) {
        try {
            withTimeout(timeout) {
                val call = apiCall.invoke()
                DataState.Success(call)
            }
        } catch (timeoutException: TimeoutCancellationException) {
            Log.e(NETWORK_ERROR, timeoutException.localizedMessage, timeoutException)
            DataState.Error(DataState.Cause.Timeout)
        } catch (ioException: IOException) {
            Log.e(SERVER_ERROR, ioException.localizedMessage, ioException)
            DataState.Error(ErrorHandler.handleException(ioException))
        } catch (httpException: HttpException) {
            Log.e(NETWORK_ERROR, httpException.localizedMessage, httpException)
            DataState.Error(ErrorHandler.handleException(httpException))
        }
    }
}
