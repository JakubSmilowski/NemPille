package com.example.nempille.domain.repository

import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

// This is the "contract" for working with users.
// ViewModels and use cases depend on THIS, not on Room directly.
interface UserRepository {

    // Observe all users with a specific role (patients or caregivers)
    fun getUsersByRole(role: UserRole): Flow<List<User>>

    // Get a single user
    suspend fun getUserById(id: Int): User?

    // Create or update a user
    suspend fun updateUser(user: User)

    // Delete a user
    suspend fun deleteUser(user: User)

    //Observe the currently logged-in user (null in no one is logged in)
    //not null - user loaded from Room using id stored in DataStore
    fun getCurrentUser(): Flow<User?>

    //create new patient user in the system
    //patient is just a user with role = PATIENT
    suspend fun createPatient(name: String): Int
}
