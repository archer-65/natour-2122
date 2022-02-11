package com.unina.natourkt.domain.use_case.auth

import com.unina.natourkt.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * This UseCase provides a fast way to check if an user is authenticated or not
 */
class AuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): Boolean {
        return authRepository.fetchCurrentSession()
    }
}