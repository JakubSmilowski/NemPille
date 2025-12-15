package com.example.nempille.data.local.dao

//DAO - data access object
//contains SQL queries that Room check at compile-time

import androidx.room.*
import com.example.nempille.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    //insert or update user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    //update user
    @Update
    suspend fun updateUser(user: UserEntity)

    //delete user
    @Delete
    suspend fun deleteUser(user: UserEntity)

    //get all users
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    //get user by role
    @Query("SELECT * FROM users WHERE role = :role")
    fun getUsersByRole(role: String): Flow<List<UserEntity>>

    //get single user by id (one-shot)
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    //observe single user by id as Flow
    // This is used together with DataStore to build "current user"
    @Query("SELECT * FROM users WHERE id = :id")
    fun observeUserById(id: Int): Flow<UserEntity?>

    //get user by email (for login & signup check)
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
}