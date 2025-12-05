package com.example.nempille.data.local.dao

import androidx.room.*
import com.example.nempille.data.local.entity.PatientEntity
import kotlinx.coroutines.flow.Flow

//DAO = Data Access Object. It contains SQL operations for the PatientEntity table
@Dao
interface PatientDao {
    //insert new patient
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPatient(patient: PatientEntity)

    //update existing patient
    @Update
    suspend fun updatePatient(patient: PatientEntity)

    //delete patient
    @Delete
    suspend fun deletePatient(patient: PatientEntity)

    //get all patients as Flow, so UI can observe changes
    @Query("SELECT * FROM patients")
    fun getAllPatients(): Flow<List<PatientEntity>>

    //get one patient by ID
    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getPatientByID(id: Int): PatientEntity?
}