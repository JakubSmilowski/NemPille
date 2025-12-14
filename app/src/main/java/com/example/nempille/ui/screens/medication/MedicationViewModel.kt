package com.example.nempille.ui.screens.medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nempille.domain.model.Medication
import com.example.nempille.domain.repository.AuthenticationRepository
import com.example.nempille.domain.repository.MedicationRepository
import com.example.nempille.ui.screens.notifications.MedicationReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNotNull

//Medication screen ViewModel now reacts to logged-in user instead of a hardcoded ID
@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository,
    private val authRepository: AuthenticationRepository,
    private val reminderScheduler: MedicationReminderScheduler
) : ViewModel() {

    //observe the current user from AuthenticationRepository
    //if he logs out - null
    //if logs in - user.id (Int)
    private val currentUserFlow: StateFlow<Int?> =
        authRepository
            .getCurrentUser()              // Flow<User?>
            .map { user -> user?.id }      //Flow<Int?>, ? for ignoring null values
            .stateIn(
                scope = viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                null                        // initial state: no user yet
            )

    //medications updates when db changes, logged user changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val medications: StateFlow<List<Medication>> =
        currentUserFlow
            .filterNotNull()                       // Wait until we have a userId
            .flatMapLatest { userId ->
                // Load medications for this user
                medicationRepository.getMedicationsForUser(userId)
            }
            .stateIn(
                scope = viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    //CREATE MEDICATION FOR CURRENT LOGGED IN USER
    fun addMedication(
        name: String,
        dosage: String,
        frequencyPerDay: Int,
        notes: String?,
        reminderHour : Int,
        reminderMinute: Int
    ) {
        viewModelScope.launch{
            //1) Wait until we get a non-null userId from the flow
            val userId: Int = currentUserFlow
                .filterNotNull()         // ignore null values
                .firstOrNull()           // suspend until we have a value
                ?: return@launch         // still null? abort safely

            //2)create domain model (id=0,room will generate real id)
            val medication = Medication(
                id = 0,            //0-let room autogenerate
                userId = userId,   //link medication to current user
                name = name,
                dosage = dosage,
                frequencyPerDay = frequencyPerDay,
                notes = notes
            )
            //3) save medication to DB
            medicationRepository.addMedication(medication)

            //4) schedule a daily reminder at user-chosen time
            val requestId = medication.hashCode()

            reminderScheduler.scheduleDailyReminder(
                requestId = requestId,
                medicationName = medication.name,
                dosage = medication.dosage,
                hour = reminderHour,
                minute = reminderMinute
            )
        }
    }
    //a test helper to insert hardcoded medication
    fun addSampleMedication() {
        viewModelScope.launch {
            val userId = currentUserFlow.value ?: return@launch  // No user logged in

            val sample = Medication(
                id = 0,                     //Room auto-generates the ID
                userId = userId,            //The REAL logged-in user
                name = "Ibuprofen",
                dosage = "1 pill",
                frequencyPerDay = 3,
                notes = "After meals"
            )

            //3)save medication to DB
            medicationRepository.addMedication(sample)

            //4) schedule a daily reminder (temp: fixed time 09:00)
            //since Medication model has no explicit time of day yet
            //a simple demo time. later can store a real time
            val hour = 9
            val minute = 0

            //for now generate simple requestId based on medication data
            //in a more advanced version, would use DB-generated ID
            val requestId = sample.hashCode()

            reminderScheduler.scheduleDailyReminder(
                requestId = requestId,
                medicationName = sample.name,
                dosage = sample.dosage,
                hour = hour,
                minute = minute
            )
        }
    }

    // LOAD ONE MEDICATION (for editing)
    suspend fun getMedication(medicationId: Int): Medication? {
        return medicationRepository.getMedicationById(medicationId)
    }

    // UPDATE MEDICATION
    fun updateMedication(medication: Medication) {
        viewModelScope.launch {
            medicationRepository.updateMedication(medication)

            // TODO: re-schedule based on new time when you introduce it
            val requestId = medication.hashCode()
            reminderScheduler.cancelReminder(requestId)
            reminderScheduler.scheduleDailyReminder(
                requestId = requestId,
                medicationName = medication.name,
                dosage = medication.dosage,
                hour = 9,
                minute = 0
            )
        }
    }

    //DELETE MEDICATION THAT BELONGS TO LOGGED IN USER
    //view-model receives medication objects from UI
    //launches coroutine
    //calls repository-dao-room-deletes the row
    //bc of the flow, medication deletes automatically

    fun deleteMedication(medication: Medication){
        viewModelScope.launch {
            medicationRepository.deleteMedication(medication)

            val requestId = medication.hashCode()
            reminderScheduler.cancelReminder(requestId)
        }
    }
}