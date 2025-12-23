package com.example.nempille.domain.repository

import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

//high-level auth operations, used by use cases/ViewModels
interface AuthenticationRepository {

    //create new user and log in
    suspend fun signup(
        name: String,
        email: String,
        role: UserRole,
        phone: String? = null,
        password: String
    ): Result<User>

    //log in existing user
    suspend fun login(email: String, password: String): Result<User>

    //logout
    suspend fun logout()

    //observe currently logged-in user (if any)
    fun getCurrentUser(): Flow<User?>

    //observe logged0in status (boolean)
    fun isLoggedIn(): Flow<Boolean>
}