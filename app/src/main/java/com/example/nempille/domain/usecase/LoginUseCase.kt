package com.example.nempille.domain.usecase

//use case - small focused action->ViewModel more readable

import com.example.nempille.domain.model.User
import com.example.nempille.domain.repository.AuthenticationRepository
import javax.inject.Inject

//Simple wrapper around repository – helps keep ViewModel clean
class LoginUseCase @Inject constructor(
    private val authRepository: AuthenticationRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.login(email, password)
    }
}
