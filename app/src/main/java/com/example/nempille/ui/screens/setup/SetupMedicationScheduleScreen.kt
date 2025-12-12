package com.example.nempille.ui.screens.setup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
// --- ADDED MISSING IMPORT ---
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nempille.ui.navigation.Screen
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupMedicationScheduleScreen(
    navController: NavController,
    medicationIndex: Int,
    viewModel: SetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val medicationCount = uiState.medications.size
    val medication = uiState.medications.getOrNull(medicationIndex) ?: return // Safeguard

    // Local state for this screen's UI
    var isDaily by remember { mutableStateOf(medication.isDaily) }
    var selectedDays by remember { mutableStateOf(medication.weeklyFrequency) }
    var additionalInfo by remember { mutableStateOf(medication.additionalInfo) }
    var schedules by remember { mutableStateOf(medication.schedules) }

    val timePickerState = rememberTimePickerState()
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onConfirm = {
                val newSchedule = PillSchedule(
                    time = java.time.LocalTime.of(timePickerState.hour, timePickerState.minute)
                )
                // Add the new time and sort the list
                schedules = (schedules + newSchedule).sortedBy { it.time }
                showTimePicker = false
            },
            state = timePickerState
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Setup for: ${medication.name}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "Medication ${medicationIndex + 1} of $medicationCount",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray
        )
        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            // --- Frequency ---
            item {
                Text("How often do you take it?", style = MaterialTheme.typography.titleLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isDaily, onClick = { isDaily = true })
                    Text("Daily")
                    Spacer(Modifier.width(16.dp))
                    RadioButton(selected = !isDaily, onClick = { isDaily = false })
                    Text("Specific days")
                }
                Spacer(Modifier.height(8.dp))
                if (!isDaily) {
                    DaySelector(selectedDays = selectedDays, onDaySelected = { day ->
                        selectedDays = if (selectedDays.contains(day)) {
                            selectedDays - day
                        } else {
                            selectedDays + day
                        }
                    })
                }
                Divider(Modifier.padding(vertical = 16.dp))
            }

            // --- Schedule Times ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("What time do you take it?", style = MaterialTheme.typography.titleLarge)
                    // --- SIMPLIFIED BUTTON ---
                    TextButton(onClick = { showTimePicker = true }) {
                        Text("Add Time")
                    }
                }
            }
            itemsIndexed(schedules) { index, schedule ->
                ScheduleItem(
                    schedule = schedule,
                    onDelete = {
                        schedules = schedules.toMutableList().apply { removeAt(index) }
                    },
                    onQuantityChange = { newQuantity ->
                        schedules = schedules.toMutableList().apply {
                            this[index] = this[index].copy(quantity = newQuantity)
                        }
                    }
                )
            }

            // --- Additional Info ---
            item {
                Divider(Modifier.padding(vertical = 16.dp))
                Text("Additional Info (Optional)", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = additionalInfo,
                    onValueChange = { additionalInfo = it },
                    label = { Text("e.g., 'take with food'") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                // 1. Update the ViewModel with the changes from this screen
                viewModel.onFrequencyChanged(medicationIndex, isDaily, selectedDays)
                viewModel.onSchedulesChanged(medicationIndex, schedules)
                viewModel.onAdditionalInfoChanged(medicationIndex, additionalInfo)

                // 2. Decide where to navigate next
                if (medicationIndex < medicationCount - 1) {
                    // There are more medications to set up
                    navController.navigate(Screen.SetupMedicationSchedule.createRoute(medicationIndex + 1))
                } else {
                    // This was the last medication, move to the notifications step
                    navController.navigate(Screen.SetupNotifications.route)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = schedules.isNotEmpty() && (isDaily || selectedDays.isNotEmpty())
        ) {
            val nextButtonText = if (medicationIndex < medicationCount - 1) {
                "Next: Set Up Medication ${medicationIndex + 2}"
            } else {
                "Next: Notification Settings"
            }
            Text(nextButtonText)
        }
    }
}

@Composable
private fun DaySelector(
    selectedDays: Set<DayOfWeek>,
    onDaySelected: (DayOfWeek) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        items(DayOfWeek.values()) { day ->
            val isSelected = selectedDays.contains(day)
            val dayInitial = day.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onDaySelected(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayInitial,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ScheduleItem(
    schedule: PillSchedule,
    onDelete: () -> Unit,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(schedule.time.format(java.time.format.DateTimeFormatter.ofLocalizedTime(java.time.format.FormatStyle.SHORT)), style = MaterialTheme.typography.bodyLarge)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Quantity: ")
            OutlinedTextField(
                value = schedule.quantity.toString(),
                onValueChange = { onQuantityChange(it.toIntOrNull() ?: 1) },
                modifier = Modifier.width(60.dp),
                keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number)
            )
        }

        // --- SIMPLIFIED BUTTON ---
        TextButton(onClick = onDelete) {
            Text("Delete", color = MaterialTheme.colorScheme.error)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    state: TimePickerState
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Time") },
        text = {
            TimePicker(state = state)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
