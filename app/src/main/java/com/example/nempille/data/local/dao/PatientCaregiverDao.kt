package com.example.nempille.data.local.dao

import androidx.room.Dao
import androidx.room.*
import com.example.nempille.data.local.entity.PatientCaregiverRelation
import com.example.nempille.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
 interface PatientCaregiverDao {

     //RELATION MANAGEMENT
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertRelation(ref: PatientCaregiverRelation)

     @Query("DELETE FROM patient_caregiver WHERE patientId = :patientId AND caregiverId = :caregiverId")
     suspend fun deleteRelation(patientId: Int, caregiverId: Int)

     //RETURNS JUST MAPPING TABLE ROWS (IDs)
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

    //UI FRIENDLY QUERIES WITH JOIN
    @Query("""
    SELECT u.* FROM users u
    INNER JOIN patient_caregiver pc 
        ON u.id = pc.patientId
    WHERE pc.caregiverId = :caregiverId
""")
    fun getPatientsForCaregiverWithUsers(
        caregiverId: Int
    ): Flow<List<UserEntity>>

    @Query("""
    SELECT u.* FROM users u
    INNER JOIN patient_caregiver pc 
        ON u.id = pc.caregiverId
    WHERE pc.patientId = :patientId
""")
    fun getCaregiversForPatientWithUsers(
        patientId: Int
    ): Flow<List<UserEntity>>
}