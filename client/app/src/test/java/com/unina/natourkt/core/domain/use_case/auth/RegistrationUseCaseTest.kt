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
    fun `when the given username contains a white space or is blank`() {
        val result = registrationUseCase.formValidator(
            username = "k s",
            email = "ksa@gmail.com",
            password = "facocero"
        )

        assertThat(false, equalTo(result))
    }

    @Test
    fun `when the given email doesn't respect the classic email pattern`() {
        val result = registrationUseCase.formValidator(
            username = "acsioadi",
            email = "ksagmail.com",
            password = "facocero"
        )

        assertThat(false, equalTo(result))
    }

    @Test
    fun `when the given password has length minus than 8 characters`() {
        val result = registrationUseCase.formValidator(
            username = "acsioadi",
            email = "ksa@gmail.com",
            password = "facoceo"
        )

        assertThat(false, equalTo(result))
    }
}