package com.example.nempille.domain.usecase

import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import com.example.nempille.domain.repository.AuthenticationRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        role: UserRole,
        phone: String?
    ): Result<User> {
        return authRepository.signup(name, email, role, phone)
    }
}