package com.unina.natourkt.core.domain.use_case.auth

import com.unina.natourkt.core.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * This UseCase provides a fast way to check if an user is authenticated or not
 * @see [AuthRepository]
 */
class GetAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): Boolean {
        return authRepository.fetchCurrentSession()
    }
}