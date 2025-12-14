package com.example.nempille.ui.screens.settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.model.User
import com.example.nempille.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class UserState(
    val currentUser: User? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // This StateFlow will hold the current user's data.
    // It automatically updates when the underlying data changes.
    val userState: StateFlow<UserState> = userRepository.getCurrentUser()
        .map { user ->
            UserState(currentUser = user, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserState(isLoading = true)
        )
}
