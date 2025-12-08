package com.example.nempille.data.repository

import com.example.nempille.data.local.dao.UserDao
import com.example.nempille.data.mapper.toDomain
import com.example.nempille.data.mapper.toEntity
import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import com.example.nempille.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Concrete implementation of UserRepository.
// It knows about Room (UserDao, UserEntity).
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    // Get users from DB, map each UserEntity -> User
    override fun getUsersByRole(role: UserRole): Flow<List<User>> =
        userDao.getUsersByRole(role.name)      // "PATIENT"
            .map { entityList ->
                entityList.map { it.toDomain() }
            }

    override suspend fun getUserById(id: Int): User? =
        userDao.getUserById(id)?.toDomain()

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
    }
}
