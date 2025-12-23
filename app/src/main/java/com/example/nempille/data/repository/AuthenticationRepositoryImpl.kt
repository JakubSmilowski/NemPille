package com.example.nempille.data.repository

import com.example.nempille.data.local.dao.UserDao
import com.example.nempille.data.mapper.toDomain
import com.example.nempille.data.mapper.toEntity
import com.example.nempille.data.local.datastore.AuthDataStore
import com.example.nempille.data.local.entity.UserEntity
import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import com.example.nempille.domain.repository.AuthenticationRepository
import com.example.nempille.ui.common.BcryptPasswordHasher
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
        phone: String?,
        password: String
    ): Result<User> {
        //1) Check if email is free
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return Result.failure(IllegalStateException("User with this email already exists"))
        }

        //2) hash password with bcrypt (salt and cost)
        val passwordHash = BcryptPasswordHasher.hash(password)

        //3)Create entity and insert into DB
        val entity = UserEntity(
            id = 0,
            name = name,
            age = null,
            phoneNumber = phone,
            role = role.name,
            email = email,
            passwordHash = passwordHash
        )
        userDao.insertUser(entity)

        //4)Read it back (now has generated id)
        val saved = userDao.getUserByEmail(email)
            ?: return Result.failure(IllegalStateException("Failed to create user"))

        val user = saved.toDomain()

        //5)Save session to DataStore
        authDataStore.saveSession(user.id, user.role)

        return Result.success(user)
    }

    override suspend fun login(email: String, password: String): Result<User> {
        //1) find user by email
        val entity = userDao.getUserByEmail(email)
            ?: return Result.failure(IllegalArgumentException("User not found"))

        // 2) Verify bcrypt
        val ok = BcryptPasswordHasher.verify(password, entity.passwordHash)
        if (!ok) {
            return Result.failure(IllegalArgumentException("Invalid password"))
        }

        val user = entity.toDomain()

        //3)save session
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
