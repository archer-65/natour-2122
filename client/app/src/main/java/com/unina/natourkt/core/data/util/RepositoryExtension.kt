package com.unina.natourkt.core.data.util

import android.util.Log
import com.unina.natourkt.core.util.Constants.NETWORK_ERROR
import com.unina.natourkt.core.util.Constants.SERVER_ERROR
import com.unina.natourkt.core.util.DataState
import com.unina.natourkt.core.util.ErrorHandler
import com.unina.natourkt.core.data.util.NetworkConstants.NETWORK_TIMEOUT
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): DataState<T> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                DataState.Success(apiCall.invoke())
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
