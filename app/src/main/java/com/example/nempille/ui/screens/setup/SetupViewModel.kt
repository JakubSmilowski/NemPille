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

data class PillSchedule(
    val time: LocalTime,
    val quantity: Int = 1
)

// Holds all the setup info for a single medication
data class MedicationSetupState(
    val name: String = "",
    val isDaily: Boolean = true,
    val weeklyFrequency: Set<DayOfWeek> = emptySet(),
    val schedules: List<PillSchedule> = emptyList(),
    val additionalInfo: String = ""
)
data class SetupState(
    // User profile
    val role: UserRole? = null,
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


    fun onRoleSelected(role: UserRole) {
        _uiState.update { it.copy(role = role) }
    }


    // Called from SetupProfileScreen
    fun onProfileInfoChanged(name: String, email: String) {
        _uiState.update { it.copy(name = name, email = email) }
    }

    // Called from SetupAgeAndPillCountScreen
    fun onAgeAndPillCountChanged(age: String, count: String) {
        _uiState.update { it.copy(age = age, pillCount = count) }
    }

    // Prepares the list of medication states before navigating to the name screen
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

    // Add this function to SetupViewModel.kt

    fun onCaregiverInfoChanged(wantsToNotify: Boolean, number: String) {
        _uiState.update { it.copy(wantsToNotifyCaregiver = wantsToNotify, caregiverMobile = number) }
    }

    //This saves everyhthing to the database hopefully
    fun completeSetup() {
        viewModelScope.launch {
            val state = _uiState.value
            // TODO: Add validation to ensure all required fields are filled

            // 1. Create and save the user
            val newUser = User(
                id = 0,
                name = state.name,
                email = state.email,
                role = UserRole.PATIENT,
                age = state.age.toIntOrNull(),
                phone = if (state.wantsToNotifyCaregiver) state.caregiverMobile else null)
            userRepository.updateUser(newUser)

            // 2. Get the new user's ID to associate medications
            val savedUser = userRepository.getUserByEmail(state.email)
            val userId = savedUser?.id ?: return@launch

            // 3. Save all the configured medications
            state.medications.forEach { medSetup ->
                val newMed = Medication(
                    userId = userId,
                    name = medSetup.name,
                    // TODO: Map your detailed schedule to the simpler Medication model
                    dosage = "See schedule",
                    frequencyPerDay = medSetup.schedules.size,
                    notes = medSetup.additionalInfo
                )
                medicationRepository.addMedication(newMed)
            }

            // 4. Log the user in to create a session
            loginUseCase(state.email)

            // 5. Update state to trigger navigation
            _uiState.update { it.copy(setupFinished = true) }
        }
    }
}
