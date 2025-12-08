package com.example.nempille.data.mapper

import com.example.nempille.data.local.entity.MedicationEntity
import com.example.nempille.domain.model.Medication

//Room returns MedicationEntity (database shape),
//but ViewModel wants Medication (domain shape)

//functions to convert between them

// Convert MedicationEntity (Room DB object) -> Medication (domain model)
//use when READING from the DATABASE

fun MedicationEntity.toDomain(): Medication {
    return Medication(
        id = this.id,
        userId = this.userId,
        name = this.name,
        dosage = this.dosage,
        frequencyPerDay = this.frequencyPerDay,
        notes = this.notes
    )
}
// Convert Medication (domain model) -> MedicationEntity (Room DB object).
// use when WRITING to the DATABASE

fun Medication.toEntity(): MedicationEntity{
    return MedicationEntity(
        id = this.id,
        userId = this.userId,
        name = this.name,
        dosage = this.dosage,
        frequencyPerDay = this.frequencyPerDay,
        notes = this.notes
    )
}