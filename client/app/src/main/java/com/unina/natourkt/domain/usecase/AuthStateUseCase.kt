package com.unina.natourkt.domain.usecase

import com.unina.natourkt.domain.repository.AuthRepository
import javax.inject.Inject

class AuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(): Boolean {
        return authRepository.fetchCurrentSession()
    }
}