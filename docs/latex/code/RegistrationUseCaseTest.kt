package com.unina.natourkt.core.domain.use_case.auth

import com.unina.natourkt.core.domain.repository.AuthRepository
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegistrationUseCaseTest {

    @InjectMocks
    private lateinit var registrationUseCase: RegistrationUseCase

    @Mock
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        registrationUseCase = RegistrationUseCase(repository)
    }

    @Test
    fun `WECT 1 - Simple pattern, username with length greater than or equal to 3, password with length greater than or equal to 8`() {
        val result = registrationUseCase.formValidator(
            email = "mattia@rossi.org",
            username = "kotlin",
            password = "coroutines"
        )

        assertTrue(result)
    }

    @Test
    fun `WECT 2 - Email's with special characters, username length greater than or equal to 3, password with length greater than or equal to 8`() {
        val result = registrationUseCase.formValidator(
            email = "mari-o.%o@live.it",
            username = "torvalds4ever",
            password = "redhatlinuxenterprise"
        )

        assertTrue(result)
    }

    @Test
    fun `WECT 3 - More than one @ in email, username with length less than 3, password with length less than 8`() {
        val result = registrationUseCase.formValidator(
            email = "bianca@@unina.com",
            username = "bc",
            password = "ingsw"
        )

        assertFalse(result)
    }

    @Test
    fun `WECT 4 - Two consecutive points before top-level domain, username with length greater than or equal to 3, password with length greater than or equal to 8`() {
        val result = registrationUseCase.formValidator(
            email = "martin.fowler@tdd..us",
            username = "mfowler",
            password = "domaindrivendesign"
        )

        assertFalse(result)
    }

    @Test
    fun `WECT 5 - One point after @, username with length less than 3, password with length greater than or equal to 8`() {
        val result = registrationUseCase.formValidator(
            email = "kentbeck@.yahoo.com",
            username = "ken",
            password = "refactoring-man"
        )

        assertFalse(result)
    }

    @Test
    fun `WECT 6 - One point before @, username with length greater than or equal to 3, password with length less than 8`() {
        val result = registrationUseCase.formValidator(
            email = "robertc.martin.@clean.code",
            username = "cc",
            password = "unclebob-deathmarchphase"
        )

        assertFalse(result)
    }
}