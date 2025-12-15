package com.example.nempille.ui.screens.patient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.model.User
import com.example.nempille.domain.repository.UserRepository
import com.example.nempille.domain.usecase.CreatePatientAndAttachToCaregiverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPatientViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val createPatientAndAttach: CreatePatientAndAttachToCaregiverUseCase
) : ViewModel() {

    //Current logged-in caregiver
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    //text fields state
    val patientName = MutableStateFlow("")
    val relationToPatient = MutableStateFlow("")

    init {
        // Load current logged-in user once
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }

    //create a new Patient user and link to current caregiver
    fun onSaveClicked(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val caregiver = _currentUser.value

        // Some basic validation
        if (caregiver == null) {
            onError("No caregiver logged in")
            return
        }
        if (patientName.value.isBlank()) {
            onError("Patient name cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                createPatientAndAttach(
                    caregiverId = caregiver.id,
                    patientName = patientName.value,
                    relationToPatient = relationToPatient.value.ifBlank { "unknown" }
                )

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
}
