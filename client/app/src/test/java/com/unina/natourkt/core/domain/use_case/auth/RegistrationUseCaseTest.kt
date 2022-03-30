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

    /**
     * EMAIL
     * 1. Prefisso con caratteri speciali + . _ % - VALIDO
     * 2. Più di una @ NON VALIDO
     * 3. Pattern prefix@example.net VALIDO
     * 4. Due punti consecutivi prima del top-level domain NON VALIDO
     * 5. Punto dopo @ NON VALIDO
     * 6. Punto prima di @ NON VALIDO
     *
     * USERNAME
     * 1. Contiene spazi NON VALIDO
     * 2. Almeno 3 caratteri VALIDO
     * 3. Meno di 3 caratteri NON VALIDO
     *
     * PASSWORD
     * 1. Almeno 8 caratteri VALIDO
     * 2. Meno di 8 caratteri NON VALIDO
     */

    @InjectMocks
    private lateinit var registrationUseCase: RegistrationUseCase

    @Mock
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        registrationUseCase = RegistrationUseCase(repository)
    }

    @Test
    fun `WECT 1 - Email's with special character, username length greater than or equal to 3, password with length greater than or equal to 8`() {
        val result = registrationUseCase.formValidator(
            email = "mari-o.%o@live.it",
            username = "bianca",
            password = "mazdamx5jap"
        )

        assertTrue(result)
    }

    @Test
    fun `WECT 2 - More than one @ in email, username with length less than 3, password with length less than 8`() {
        val result = registrationUseCase.formValidator(
            email = "bianca@@maserati.com",
            username = "ab",
            password = "ingsw"
        )

        assertFalse(result)
    }

    @Test
    fun `WECT 3 - Simple pattern, username with length greater than or equal to 3, password with length greater than or equal to 8`() {
        val result = registrationUseCase.formValidator(
            email = "mattia@rossi.org",
            username = "kotlin",
            password = "coroutines"
        )

        assertTrue(result)
    }

    @Test
    fun `WECT 4 - Two consecutive points, username with length greater than or equal to 3, password with length greater than or equal to 8`() {
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
    }
}