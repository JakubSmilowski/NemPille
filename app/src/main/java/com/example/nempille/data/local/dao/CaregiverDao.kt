package com.example.nempille.data.local.dao

import androidx.room.*
import com.example.nempille.data.local.entity.CaregiverEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CaregiverDao {
    //insert caregiver
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaregiver(patient: CaregiverEntity)

    //update existing patient row
    @Update
    suspend fun updateCaregiver(patient: CaregiverEntity)

    //delete patient from the table
    @Delete
    suspend fun deleteCaregiver(patient: CaregiverEntity)

    //get all patients
    @Query("SELECT * FROM patients")
    fun getAllCaregivers(): Flow<List<CaregiverEntity>>

    //get one patient by id
    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getCaregiverById(id: Int): CaregiverEntity
}