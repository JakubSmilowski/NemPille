package com.example.nempille.ui.splash

// ViewModel that decides where to go after splash:
//If a user is logged in -> Home
//Otherwise -> Login

//idea - SplashVM asks backend 'if user is there' and then
//chooses where to go

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.repository.AuthenticationRepository
import com.example.nempille.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    //Target route that splash should navigate to
    //null = still deciding/loading
    private val _targetRoute = MutableStateFlow<String?>(null)
    val targetRoute: StateFlow<String?> = _targetRoute.asStateFlow()

    init {
        //When ViewModel is created, decide once where to go
        viewModelScope.launch {
            //get current user one time from the repository
            //if prefer, could also observe a Flow forever
            val currentUser = authRepository.getCurrentUser().firstOrNull()

            _targetRoute.value = if (currentUser != null) {
                //User exists -> to Home screen
                Screen.Home.route
            } else {
                //No user -> to Login screen
                Screen.WelcomeScreen.route
            }
        }
    }
}
