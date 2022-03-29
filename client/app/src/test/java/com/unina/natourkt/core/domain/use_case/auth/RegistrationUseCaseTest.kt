package com.unina.natourkt.core.domain.use_case.auth

import com.unina.natourkt.core.domain.repository.AuthRepository
import com.unina.natourkt.core.util.DataState
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class RegistrationUseCaseTest {

    private lateinit var registrationUseCase: RegistrationUseCase

    @Mock
    private lateinit var repository: AuthRepository

    @Before
    fun setup() {
        registrationUseCase = RegistrationUseCase(repository)
    }

    @Test
    fun `when the given username contains a white space or is blank, it should return InvalidUsername`() {
        val result = registrationUseCase.formValidator(
            username = "k s",
            email = "ksa@gmail.com",
            password = "facocero"
        )

        assertThat(result.error, equalTo(DataState.Cause.InvalidUsername))
    }

    @Test
    fun `when the given email doesn't respect the classic email pattern, it should return InvalidEmail`() {
        val result = registrationUseCase.formValidator(
            username = "acsioadi",
            email = "ksagmail.com",
            password = "facocero"
        )

        assertThat(result.error, equalTo(DataState.Cause.InvalidEmail))
    }

    @Test
    fun `when the given password has length minus than 8 characters, it should return InvalidPassword`() {
        val result = registrationUseCase.formValidator(
            username = "acsioadi",
            email = "ksa@gmail.com",
            password = "facoceo"
        )

        assertThat(result.error, equalTo(DataState.Cause.InvalidPassword))
    }
}