package com.example.nempille.data.local.entity

//join table, cross reference between patient and caregiver
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "patient_caregiver",
    primaryKeys = ["patientId", "caregiverId"]
)
data class PatientCaregiverRelation(
    val patientId: Int, //FK users.id with role PATIENT
    val caregiverId: Int, //FK with users.id where role is CAREGIVER

    //'daughter', 'nurse', 'friend' etc
    val relationToPatient: String
)