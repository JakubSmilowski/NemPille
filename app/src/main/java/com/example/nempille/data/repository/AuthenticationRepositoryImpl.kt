package com.example.nempille.data.repository

import com.example.nempille.data.local.dao.UserDao
import com.example.nempille.data.mapper.toDomain
import com.example.nempille.data.mapper.toEntity
import com.example.nempille.data.local.datastore.AuthDataStore
import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import com.example.nempille.domain.repository.AuthenticationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

//Concrete implementation of AuthenticationRepository
// Knows about Room + DataStore.
class AuthenticationRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authDataStore: AuthDataStore
) : AuthenticationRepository {

    override suspend fun signup(
        name: String,
        email: String,
        role: UserRole,
        phone: String?
    ): Result<User> {
        //Check if email is free
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(IllegalStateException("User with this email already exists"))
        }

        //Create entity and insert into DB
        val entity = com.example.nempille.data.local.entity.UserEntity(
            name = name,
            email = email,
            phoneNumber = phone,
            role = role.name
        )
        userDao.insertUser(entity)

        //Read it back (now has generated id)
        val saved = userDao.getUserByEmail(email)
            ?: return Result.failure(IllegalStateException("Failed to create user"))

        val user = saved.toDomain()

        //Save session to DataStore
        authDataStore.saveSession(user.id, user.role)

        return Result.success(user)
    }

    override suspend fun login(email: String): Result<User> {
        //find user by email
        val entity = userDao.getUserByEmail(email)
            ?: return Result.failure(IllegalArgumentException("User not found"))

        val user = entity.toDomain()

        //save session
        authDataStore.saveSession(user.id, user.role)

        return Result.success(user)
    }

    override suspend fun logout() {
        authDataStore.clearSession()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentUser(): Flow<User?> {
        // We watch userId in DataStore and map it to User from DB
        return authDataStore.userIdFlow().flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                flow {
                    val entity = userDao.getUserById(id)
                    emit(entity?.toDomain())
                }
            }
        }
    }

    override fun isLoggedIn(): Flow<Boolean> =
        authDataStore.isLoggedInFlow()
}
