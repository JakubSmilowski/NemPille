package com.example.nempille.data.local.database
//main database class
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nempille.data.local.dao.MedicationDao
import com.example.nempille.data.local.dao.PatientCaregiverDao
import com.example.nempille.data.local.dao.UserDao
import com.example.nempille.data.local.entity.MedicationEntity
import com.example.nempille.data.local.entity.UserEntity
import com.example.nempille.data.local.entity.PatientCaregiverRelation

// Version = database schema version. CHANGE it when structure changes
// exportSchema = false disables saving schema files
@Database(
    entities = [
        MedicationEntity::class,
        UserEntity::class,
        PatientCaregiverRelation::class],
    version = 4, //change each time
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // Provide access to DAOs

    //DAO medication
    abstract fun medicationDao(): MedicationDao

    //DAO user
    abstract fun userDao(): UserDao

    //dao patient_caregiver
    abstract fun patientCaregiverDao(): PatientCaregiverDao
}