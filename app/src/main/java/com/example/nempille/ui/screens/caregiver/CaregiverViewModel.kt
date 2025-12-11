package com.example.nempille.ui.screens.caregiver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.model.User
import com.example.nempille.domain.model.UserRole
import com.example.nempille.domain.repository.UserRepository
import com.example.nempille.domain.usecase.GetPatientsForCaregiverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CaregiverViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getPatientsForCaregiver: GetPatientsForCaregiverUseCase
) : ViewModel() {

    // Logged-in user
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    // Patients of this caregiver
    @OptIn(ExperimentalCoroutinesApi::class)
    val patients: StateFlow<List<User>> =
        currentUser
            .filterNotNull()
            .flatMapLatest { user ->
                if (user.role == UserRole.CAREGIVER)
                    getPatientsForCaregiver(user.id)
                else flowOf(emptyList())
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Observe logged-in user
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { user ->
                _currentUser.value = user
            }
        }
    }
}