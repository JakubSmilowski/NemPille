package com.example.nempille.ui.screens.medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.model.Medication
import com.example.nempille.domain.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

//viewModel that receives MedicationRepository via Hilt Injection
//exposes Flow of <List> Medication for UI
//function to add a test medication to DB

//hilt-managed View-Model, meaning - it will provide repository
@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val repository: MedicationRepository
) : ViewModel() {
    //NOW: hardcoded user ID
    //later with login/auth
    private val currentUserId: Long = 1L

    //expose list of medications, so UI observes it
    //.getMedicationsForUser returns Flow<List<Medication>>
    //stateIn converts it in StetFlow with initial empty list
    val medications: StateFlow<List<Medication>> =
        repository
            .getMedicationsForUser(currentUserId)
            .stateIn(
                scope = viewModelScope,              // ViewModel's coroutine scope
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    // Simple helper function to add a sample medication into the database
    // This lets us see Room working without building a full "Add medication" form yet
    fun addSampleMedication() {
        viewModelScope.launch {
            val sample = Medication(
                id = 0L,               // 0 means: let Room auto-generate ID
                userId = currentUserId,
                name = "Ibuprofen",
                dosage = "1 pill",
                frequencyPerDay = 3,
                notes = "After meals"
            )

            repository.addMedication(sample)
        }
    }
}