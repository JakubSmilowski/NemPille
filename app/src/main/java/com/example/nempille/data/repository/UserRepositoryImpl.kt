package com.example.nempille.data.repository

import com.example.nempille.data.local.dao.UserDao
import com.example.nempille.data.local.datastore.AuthDataStore
import com.example.nempille.data.mapper.toDomain
import com.example.nempille.data.mapper.toEntity
import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import com.example.nempille.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Concrete implementation of UserRepository.
// It knows about Room (UserDao, UserEntity) and AuthDataStore
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val authDataStore: AuthDataStore
) : UserRepository {

    // Get users from DB, map each UserEntity -> domain User
    override fun getUsersByRole(role: UserRole): Flow<List<User>> =
        userDao.getUsersByRole(role.name)      // "PATIENT"
            .map { entityList ->
                entityList.map { it.toDomain() }
            }

    override suspend fun getUserById(id: Int): User? =
        userDao.getUserById(id)?.toDomain()

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
        // We don't need to touch DataStore here, it only stores user_id / role
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.toEntity())
        //later: could also clear session if the logged-in user was deleted
    }

    //observe currently logged in user, using DataStore,Room,mappers
    //AuthDataStore.userIdFlow() -> current user id (Int?)
    //for each id: if null  -> emit null (no logged in user)
    //if not null -> observe that user from Room as Flow<UserEntity?>, map it to domain User?
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getCurrentUser(): Flow<User?> =
        authDataStore.userIdFlow() // Flow<Int?>
            .flatMapLatest { userId: Int? ->
                if (userId == null) {
                    // Not logged in -> just emit null
                    flowOf(null)
                } else {
                    // Logged in: observe this user from DB
                    userDao.observeUserById(userId) // Flow<UserEntity?>
                        .map { entity ->
                            entity?.toDomain()
                        }
                }
            }
}
