package com.example.nempille.domain.usecase

import com.example.nempille.domain.repository.PatientCaregiverRepository
import javax.inject.Inject

class GetPatientsForCaregiverUseCase @Inject constructor(
    private val repository: PatientCaregiverRepository
) {
    operator fun invoke(caregiverId: Int) =
        repository.getPatientsForCaregiver(caregiverId)
}