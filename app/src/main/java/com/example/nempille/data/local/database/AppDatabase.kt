package com.example.nempille.data.local.database
//main database class
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nempille.data.local.dao.CaregiverDao
import com.example.nempille.data.local.dao.MedicationDao
import com.example.nempille.data.local.dao.PatientDao
import com.example.nempille.data.local.entity.CaregiverEntity
import com.example.nempille.data.local.entity.MedicationEntity
import com.example.nempille.data.local.entity.PatientEntity

// Version = database schema version. CHANGE it when structure changes
// exportSchema = false disables saving schema files
@Database(
    entities = [
        MedicationEntity::class,
        PatientEntity:: class,
        CaregiverEntity:: class],
    version = 2, //schema changed, changed from 1 to 2
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Provide access to DAOs

    //DAO medication
    abstract fun medicationDao(): MedicationDao

    //DAO patient
    abstract fun patientDao(): PatientDao

    //Dao caregiver
    abstract fun caregiverDao(): CaregiverDao
}