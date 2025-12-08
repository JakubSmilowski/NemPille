package com.example.nempille.data.local.dao

import androidx.room.*
import com.example.nempille.data.local.entity.PatientCaregiverRelation
import kotlinx.coroutines.flow.Flow

@Dao
 interface PatientCaregiverDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertRelation(ref: PatientCaregiverRelation)

     @Query("DELETE FROM patient_caregiver WHERE patientId = :patientId AND caregiverId = :caregiverId")
     suspend fun deleteRelation(patientId: Int, caregiverId: Int)

    @Query("""
        SELECT * FROM patient_caregiver 
        WHERE patientId = :patientId
    """)
    fun getCaregiversForPatient(patientId: Int): Flow<List<PatientCaregiverRelation>>

    @Query("""
        SELECT * FROM patient_caregiver 
        WHERE caregiverId = :caregiverId
    """)
    fun getPatientsForCaregiver(caregiverId: Int): Flow<List<PatientCaregiverRelation>>
}