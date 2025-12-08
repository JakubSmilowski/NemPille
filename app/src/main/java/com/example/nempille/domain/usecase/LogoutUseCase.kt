package com.example.nempille.domain.usecase

import com.example.nempille.domain.repository.AuthenticationRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}