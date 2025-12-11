package com.example.nempille.data.local.dao

//DAO - data access object
//contains SQL queries that Room check at compile-time

import androidx.room.*
import com.example.nempille.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao{
    //returns ALL medications for one user-patient
    //flow lets UI automatically update when data changes
    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY id ASC")
    fun getMedicationsForUser(userId: Int): Flow<List<MedicationEntity>>

    //insert new medication
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    //update existing medication
    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    //Delete
    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)

    //Get ALL medications
    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    //GET MEDICATION BY ID
    @Query("SELECT * FROM medications WHERE id = :id LIMIT 1")
    suspend fun getMedicationById(id: Int): MedicationEntity?

}

//room+flow - automatic UI updates