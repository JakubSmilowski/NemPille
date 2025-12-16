package com.example.nempille.ui.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class IntakeStatus {
    TAKEN,
    MISSED,
    DUE
}

data class MedicationIntake(
    val medicationName: String,
    val dosage: String,
    val timestamp: Date,
    val status: IntakeStatus
)

@Composable
fun HistoryScreen(navController: NavController) {
    val mockHistory = generateMockHistory()

    val today = Calendar.getInstance()
    val week = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -7) }
    val month = Calendar.getInstance().apply { add(Calendar.MONTH, -1) }

    val historyToday = mockHistory.filter { isSameDay(it.timestamp, today.time) }
    val historyThisWeek = mockHistory.filter { it.timestamp.after(week.time) && !isSameDay(it.timestamp, today.time) }
    val historyThisMonth = mockHistory.filter { it.timestamp.after(month.time) && it.timestamp.before(week.time) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Medication History", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            TodayHistorySection(historyToday)
        }

        item {
            AggregatedHistorySection("This Week", historyThisWeek)
        }
        
        item {
            AggregatedHistorySection("This Month", historyThisMonth)
        }
    }
}

@Composable
fun TodayHistorySection(history: List<MedicationIntake>) {
    var expanded by remember { mutableStateOf(true) } // Expanded by default

    SectionHeader("Today", expanded) { expanded = !expanded }

    AnimatedVisibility(visible = expanded) {
        Column {
            history.sortedBy { it.status }.forEach { intake ->
                HistoryItem(intake)
            }
        }
    }
}

@Composable
fun AggregatedHistorySection(title: String, history: List<MedicationIntake>) {
    var expanded by remember { mutableStateOf(false) }
    val missedDoses = history.filter { it.status == IntakeStatus.MISSED }
    val missedByDay = missedDoses.groupBy { SimpleDateFormat("EEEE, MMM d", Locale.getDefault()).format(it.timestamp) }

    SectionHeader(title, expanded) { expanded = !expanded }

    AnimatedVisibility(visible = expanded) {
        Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
            if (missedByDay.isEmpty()) {
                Text("No missed doses.", style = MaterialTheme.typography.bodyLarge)
            } else {
                missedByDay.forEach { (day, intakes) ->
                    Text("$day: ${intakes.size} missed dose(s)", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, expanded: Boolean, onClick: () -> Unit) {
    val arrow = if (expanded) "▼" else "▶"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$arrow $title", style = MaterialTheme.typography.headlineMedium)
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun HistoryItem(intake: MedicationIntake) {
    val color = when (intake.status) {
        IntakeStatus.TAKEN -> Color(0xFF388E3C) // Green
        IntakeStatus.MISSED -> Color(0xFFD32F2F) // Red
        IntakeStatus.DUE -> Color(0xFF1976D2) // Blue
    }
    val statusText = when (intake.status) {
        IntakeStatus.TAKEN -> "Taken at ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(intake.timestamp)}"
        IntakeStatus.MISSED -> "Missed at ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(intake.timestamp)}"
        IntakeStatus.DUE -> "Due at ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(intake.timestamp)}"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(intake.medicationName, style = MaterialTheme.typography.bodyLarge)
                Text(intake.dosage, style = MaterialTheme.typography.bodyMedium)
            }
            Text(statusText, style = MaterialTheme.typography.bodyMedium, color = color)
        }
    }
}

private fun generateMockHistory(): List<MedicationIntake> {
    val cal = Calendar.getInstance()
    return listOf(
        // Today
        MedicationIntake("Ibuprofen", "1 tablet", cal.apply { set(Calendar.HOUR_OF_DAY, 8) }.time, IntakeStatus.TAKEN),
        MedicationIntake("Paracetamol", "2 tablets", cal.apply { set(Calendar.HOUR_OF_DAY, 12) }.time, IntakeStatus.MISSED),
        MedicationIntake("Aspirin", "1 tablet", cal.apply { set(Calendar.HOUR_OF_DAY, 20) }.time, IntakeStatus.DUE),

        // This Week
        MedicationIntake("Vitamin C", "1 tablet", cal.apply { add(Calendar.DAY_OF_YEAR, -1) }.time, IntakeStatus.TAKEN),
        MedicationIntake("Vitamin D", "1 tablet", cal.apply { add(Calendar.DAY_OF_YEAR, -2); set(Calendar.HOUR_OF_DAY, 9) }.time, IntakeStatus.MISSED),
        MedicationIntake("Vitamin D", "1 tablet", cal.apply { set(Calendar.HOUR_OF_DAY, 18) }.time, IntakeStatus.MISSED),
        MedicationIntake("Iron Supplement", "1 capsule", cal.apply { add(Calendar.DAY_OF_YEAR, -4) }.time, IntakeStatus.TAKEN),

        // This Month
        MedicationIntake("Antibiotic", "1 capsule", cal.apply { add(Calendar.DAY_OF_YEAR, -8) }.time, IntakeStatus.MISSED),
        MedicationIntake("Probiotic", "1 capsule", cal.apply { add(Calendar.DAY_OF_YEAR, -10) }.time, IntakeStatus.TAKEN),
        MedicationIntake("Allergy Pill", "1 tablet", cal.apply { add(Calendar.DAY_OF_YEAR, -15) }.time, IntakeStatus.MISSED)
    )
}

private fun isSameDay(date1: Date, date2: Date): Boolean {
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}
