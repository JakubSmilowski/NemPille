package com.example.nempille.domain.repository

import com.example.nempille.domain.model.User
import kotlinx.coroutines.flow.Flow

// Domain interface: describes WHAT we need, not HOW.
interface PatientCaregiverRepository {

    // Caregiver sees list of patients
    fun getPatientsForCaregiver(caregiverId: Int): Flow<List<User>>

    // Later: patient sees list of caregivers
    fun getCaregiversForPatient(patientId: Int): Flow<List<User>>
}