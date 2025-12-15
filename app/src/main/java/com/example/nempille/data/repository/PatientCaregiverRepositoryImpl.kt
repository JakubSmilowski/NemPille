package com.example.nempille.data.repository

import com.example.nempille.data.local.dao.PatientCaregiverDao
import com.example.nempille.data.local.entity.PatientCaregiverRelation
import com.example.nempille.data.mapper.toDomain
import com.example.nempille.domain.model.User
import com.example.nempille.domain.repository.PatientCaregiverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Implementation of domain repository using Room DAO
//wrapping Room DAO and mapping entities to domain models
@Singleton
class PatientCaregiverRepositoryImpl @Inject constructor(
    private val dao: PatientCaregiverDao
) : PatientCaregiverRepository {

    //// Get all patients assigned to a caregiver
    override fun getPatientsForCaregiver(caregiverId: Int): Flow<List<User>> {
        // DAO returns List<UserEntity>, map → List<User>
        return dao.getPatientsForCaregiverWithUsers(caregiverId)
            .map { list -> list.map { it.toDomain() } }
    }

    //// Get all caregivers assigned to a patient
    override fun getCaregiversForPatient(patientId: Int): Flow<List<User>> {
        return dao.getCaregiversForPatientWithUsers(patientId)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addRelation(
        patientId: Int,
        caregiverId: Int,
        relationToPatient: String
    ) {
        //create entity for join table and insert int
        val relation = PatientCaregiverRelation(
            patientId = patientId,
            caregiverId = caregiverId,
            relationToPatient = relationToPatient
        )
        dao.insertRelation(relation)
    }
}