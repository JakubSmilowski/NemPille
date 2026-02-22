package com.example.nempille.data.local.dao

import androidx.room.*
import com.example.nempille.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

/**
 * CONCEPT: Data Access Object (DAO)
 * 
 * How it works:
 * DAOs are the main component of Room that are responsible for 
 * defining the methods that access the database.
 */
@Dao
interface MedicationDao {

    /**
     * CONCEPT: Room with Flow (Reactive UI)
     * 
     * How it works:
     * By returning a Flow, Room will automatically emit a new 
     * list of medications whenever the 'medications' table 
     * changes. This allows the UI to update in real-time.
     */
    @Query("SELECT * FROM medications WHERE userId = :userId ORDER BY id ASC")
    fun getMedicationsForUser(userId: Int): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Int): MedicationEntity?

    /**
     * CONCEPT: Coroutines (suspend functions)
     * 
     * How it works:
     * 'suspend' ensures this database operation is performed on 
     * a background thread, preventing the main UI thread from 
     * freezing during a write operation.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedication(medication: MedicationEntity)

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Delete
    suspend fun deleteMedication(medication: MedicationEntity)
}
