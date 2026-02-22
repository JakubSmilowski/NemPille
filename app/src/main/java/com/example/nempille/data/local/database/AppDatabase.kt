package com.example.nempille.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nempille.data.local.dao.MedicationDao
import com.example.nempille.data.local.dao.PatientCaregiverDao
import com.example.nempille.data.local.dao.UserDao
import com.example.nempille.data.local.entity.MedicationEntity
import com.example.nempille.data.local.entity.UserEntity
import com.example.nempille.data.local.entity.PatientCaregiverRelation

/**
 * CONCEPT: Room Database
 * 
 * How it works:
 * This class serves as the main access point to the persisted data. 
 * - @Database: Defines the list of @Entity classes and the version.
 * - RoomDatabase: The base class provided by Room.
 * - Abstract DAO methods: Room generates the implementation for these.
 */
@Database(
    entities = [
        MedicationEntity::class,
        UserEntity::class,
        PatientCaregiverRelation::class],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun medicationDao(): MedicationDao
    abstract fun userDao(): UserDao
    abstract fun patientCaregiverDao(): PatientCaregiverDao
}
