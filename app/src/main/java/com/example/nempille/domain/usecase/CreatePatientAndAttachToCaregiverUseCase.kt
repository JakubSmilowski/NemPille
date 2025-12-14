package com.example.nempille.domain.usecase

import com.example.nempille.domain.repository.PatientCaregiverRepository
import com.example.nempille.domain.repository.UserRepository
import javax.inject.Inject

//Use case that:
//1) Creates a new patient user
//2) Attaches this patient to a caregiver via relation table
class CreatePatientAndAttachToCaregiverUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val patientCaregiverRepository: PatientCaregiverRepository
) {

    /**
     * @param caregiverId ID of the caregiver (current logged-in user)
     * @param patientName name of the new patient
     * @param relationToPatient how caregiver relates to patient ("mother", "client", etc)
     */
    suspend operator fun invoke(
        caregiverId: Int,
        patientName: String,
        relationToPatient: String
    ) {
        // 1. Create new patient user and get id
        val patientId = userRepository.createPatient(patientName)

        // 2. Attach patient to caregiver with given relation
        patientCaregiverRepository.addRelation(
            patientId = patientId,
            caregiverId = caregiverId,
            relationToPatient = relationToPatient
        )
    }
}
