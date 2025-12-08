package com.example.nempille.data.repository

import com.example.nempille.data.local.dao.MedicationDao
import com.example.nempille.data.mapper.toDomain
import com.example.nempille.data.mapper.toEntity
import com.example.nempille.domain.model.Medication
import com.example.nempille.domain.repository.MedicationRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Concrete implementation of MedicationRepository that uses Room via MedicationDao
class MedicationRepositoryImpl @Inject constructor(
    private val medicationDao: MedicationDao   // DAO injected via constructor
) : MedicationRepository {

    // Get medications as a Flow of Entities from DAO,
    // then map them to a Flow of domain Medications.
    override fun getMedicationsForUser(userId: Long): Flow<List<Medication>> {
        return medicationDao
            .getMedicationsForUser(userId)         // Flow<List<MedicationEntity>>
            .map { entityList ->
                entityList.map { entity ->
                    entity.toDomain()              // map each Entity -> Domain
                }
            }
    }

    // Insert a new medication using a suspend function
    override suspend fun addMedication(medication: Medication) {
        medicationDao.insertMedication(
            medication.toEntity()                  // Domain -> Entity
        )
    }

    // Update existing medication
    override suspend fun updateMedication(medication: Medication) {
        medicationDao.updateMedication(
            medication.toEntity()
        )
    }

    // Delete medication
    override suspend fun deleteMedication(medication: Medication) {
        medicationDao.deleteMedication(
            medication.toEntity()
        )
    }
}

//private val medicationDao: MedicationDao
//This class depends on the DAO
//For now create MedicationRepositoryImpl manually and pass a DAO instance

//Flow<List<MedicationEntity>>.map { ... }
//map from Kotlin Flows
//Lets transform the flow of entities into a flow of domain models as data changes over time

//suspend fun
//These functions run in coroutines; they’re safe for database operations.
//ViewModel will call them inside a viewModelScope.launch { ... }.