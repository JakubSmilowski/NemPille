package com.example.nempille.ui.screens.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.model.*
import com.example.nempille.domain.repository.MedicationRepository
import com.example.nempille.domain.repository.UserRepository
import com.example.nempille.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Inject


// --- DATA MODELS FOR THE SETUP FLOW ---

data class PillSchedule(
    val time: LocalTime,
    val quantity: Int = 1
)

data class MedicationSetupState(
    val name: String = "",
    val isDaily: Boolean = true,
    val weeklyFrequency: Set<DayOfWeek> = emptySet(),
    val schedules: List<PillSchedule> = emptyList(),
    val additionalInfo: String = ""
)

/**
 * CONCEPT: State Hoisting & UDF (State)
 * 
 * How it works:
 * SetupState is the single source of truth for the entire setup flow. 
 * It's "hoisted" to the ViewModel so it can be shared across multiple 
 * Composable screens (Profile, Age, etc.) and survive configuration changes.
 */
data class SetupState(
    // User profile
    val name: String = "",
    val email: String = "",
    val age: String = "",

    // Medication setup
    val pillCount: String = "",
    val medications: List<MedicationSetupState> = emptyList(),

    // Caregiver settings
    val wantsToNotifyCaregiver: Boolean = false,
    val caregiverMobile: String = "",

    // Flow control
    val setupFinished: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginUseCase: LoginUseCase,
    private val medicationRepository: MedicationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupState())
    val uiState = _uiState.asStateFlow()

    /**
     * CONCEPT: Unidirectional Data Flow (UDF) (Events)
     * 
     * How it works:
     * The UI calls this function (an "event") to notify the ViewModel 
     * of a change. The ViewModel then updates its state, which flows 
     * back down to the UI to be displayed.
     */
    fun onProfileInfoChanged(name: String, email: String) {
        _uiState.update { it.copy(name = name, email = email) }
    }

    fun onAgeAndPillCountChanged(age: String, count: String) {
        _uiState.update { it.copy(age = age, pillCount = count) }
    }

    fun prepareMedicationList() {
        val count = _uiState.value.pillCount.toIntOrNull() ?: 0
        _uiState.update {
            it.copy(medications = List(count) { MedicationSetupState() })
        }
    }

    fun onFrequencyChanged(medIndex: Int, isDaily: Boolean, days: Set<DayOfWeek>) {
        _uiState.update { state ->
            val updatedMeds = state.medications.toMutableList()
            if (medIndex < updatedMeds.size) {
                updatedMeds[medIndex] = updatedMeds[medIndex].copy(isDaily = isDaily, weeklyFrequency = days)
            }
            state.copy(medications = updatedMeds)
        }
    }

    fun onSchedulesChanged(medIndex: Int, schedules: List<PillSchedule>) {
        _uiState.update { state ->
            val updatedMeds = state.medications.toMutableList()
            if (medIndex < updatedMeds.size) {
                updatedMeds[medIndex] = updatedMeds[medIndex].copy(schedules = schedules)
            }
            state.copy(medications = updatedMeds)
        }
    }

    fun onAdditionalInfoChanged(medIndex: Int, info: String) {
        _uiState.update { state ->
            val updatedMeds = state.medications.toMutableList()
            if (medIndex < updatedMeds.size) {
                updatedMeds[medIndex] = updatedMeds[medIndex].copy(additionalInfo = info)
            }
            state.copy(medications = updatedMeds)
        }
    }
    fun onMedicationNameChanged(index: Int, name: String) {
        _uiState.update { state ->
            val updatedMeds = state.medications.toMutableList()
            if (index < updatedMeds.size) {
                updatedMeds[index] = updatedMeds[index].copy(name = name)
            }
            state.copy(medications = updatedMeds)
        }
    }

    fun onCaregiverInfoChanged(wantsToNotify: Boolean, number: String) {
        _uiState.update { it.copy(wantsToNotifyCaregiver = wantsToNotify, caregiverMobile = number) }
    }
    
    /**
     * CONCEPT: Coroutines & viewModelScope
     * 
     * How it works:
     * This function saves all data to the database. We use 
     * viewModelScope.launch to run this on a background thread. 
     * If the ViewModel is destroyed (e.g., user leaves the screen), 
     * the coroutine is automatically cancelled to prevent memory leaks.
     */
    fun completeSetup() {
        viewModelScope.launch {
            val state = _uiState.value
            
            val newUser = User(
                id = 0, 
                name = state.name,
                email = state.email,
                role = UserRole.PATIENT, 
                age = state.age.toIntOrNull(),
                phone = if (state.wantsToNotifyCaregiver) state.caregiverMobile else null)
            userRepository.updateUser(newUser)

            val savedUser = userRepository.getUserByEmail(state.email)
            val userId = savedUser?.id ?: return@launch

            state.medications.forEach { medSetup ->
                val newMed = Medication(
                    userId = userId,
                    name = medSetup.name,
                    dosage = "See schedule",
                    frequencyPerDay = medSetup.schedules.size,
                    notes = medSetup.additionalInfo
                )
                medicationRepository.addMedication(newMed)
            }

            loginUseCase(state.email)

            _uiState.update { it.copy(setupFinished = true) }
        }
    }
}
