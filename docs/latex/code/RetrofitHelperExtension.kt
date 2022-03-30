package com.unina.natourkt.core.data.util

import com.unina.natourkt.core.util.DataState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.wait
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RetrofitHelperExtension {

    @get:Rule
    val coroutineRuleForTest: CoroutineTestRule = CoroutineTestRule()

    private lateinit var dispatcher: TestDispatcher

    @Before
    fun setup() {
        dispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `PATH 1 - when the TimeoutCancellationException is thrown due to short timeout value or this timeout is exceeded, it should emit Timeout Error`() {
        runTest {
            val result = retrofitSafeCall(dispatcher = dispatcher, timeout = -1L) { }

            assertThat(DataState.Cause.Timeout, equalTo(result.error))
        }
    }

    @Test
    fun `PATH 2 - when the lambda function returns without error after a network call, it should emit success with generic type data corresponding to the one given as parameter`() =
        runTest {
            val lambdaExpected = "String to expect due to no operation by lambda"
            val result = retrofitSafeCall(dispatcher = dispatcher, timeout = 5L) { lambdaExpected }

            assertThat(lambdaExpected, equalTo(result.data))
        }


    @Test
    fun `PATH 3 - when the TimeoutCancellationException is thrown due request expiration, it should emit Timeout Error`() {
        runTest {
            val result = retrofitSafeCall(dispatcher = dispatcher, timeout = 2L) { delay(2L) }

            assertThat(DataState.Cause.Timeout, equalTo(result.error))
        }
    }

    @Test
    fun `PATH 4 - when an IOException is thrown in the lambda, it should emit Network Error`() {
        runTest {
            val result =
                retrofitSafeCall(dispatcher = dispatcher, timeout = 5L) { throw IOException() }

            assertThat(DataState.Cause.NetworkError, equalTo(result.error))
        }
    }

    @Test
    fun `PATH 5 - when an HTTPException is thrown in the lambda, it should emit HTTPGeneric`() {
        runTest {
            val body =
                "{\"Request not processable\"]}".toResponseBody("application/json".toMediaType())

            val result =
                retrofitSafeCall(dispatcher = dispatcher, timeout = 5L) {
                    throw HttpException(
                        Response.error<Any>(
                            422,
                            body
                        )
                    )
                }

            assertThat(DataState.Cause.HTTPGeneric, equalTo(result.error))
        }
    }
}