# NemPille
NemPille - a mobile app for elderly and their caregivers that is connected to pill dispenser 

# NemPille Architecture & Concepts

This document outlines the key Android development concepts used in this project, providing a reference for where each concept is implemented.

### 1. User-Centered Design (UCD)
- **Structure & Navigation Flow**: The multi-step setup process is managed by the `SetupViewModel`, defining a clear user journey.
  - `app/src/main/java/com/example/nempille/ui/screens/setup/SetupViewModel.kt`
- **Error Prevention (Heuristic)**: The UI prevents invalid states, such as disabling the "Next" button until schedules are added.
  - `app/src/main/java/com/example/nempille/ui/screens/setup/SetupMedicationScheduleScreen.kt`

### 2. Jetpack Compose UI & Architecture
- **Single Activity Architecture**: The app uses a single `MainActivity` that hosts a `NavHost` for all screens.
  - `app/src/main/java/com/example/nempille/MainActivity.kt`
- **Unidirectional Data Flow (UDF)**: ViewModels expose state via `StateFlow` and receive events from the UI through public functions.
  - **State**: `SetupState` in `SetupViewModel.kt`
  - **Events**: `onProfileInfoChanged(...)` in `SetupViewModel.kt`
  - `app/src/main/java/com/example/nempille/ui/screens/setup/SetupViewModel.kt`
- **State Hoisting**: Composables like `TimePickerDialog` are made stateless by receiving state and callbacks from their parent.
  - `app/src/main/java/com/example/nempille/ui/screens/setup/SetupMedicationScheduleScreen.kt`
- **Compose Layouts**: Standard layouts like `Column`, `Row`, and `LazyColumn` are used to build all screens.
  - `app/src/main/java/com/example/nempille/ui/screens/home/HomeScreen.kt`
  - `app/src/main/java/com/example/nempille/ui/screens/caregiver/CaregiverScreen.kt`
- **Material Design 3**: The app uses the Material 3 design system, including `Scaffold`, `MaterialTheme`, and M3 components.
  - `app/src/main/java/com/example/nempille/ui/theme/Theme.kt`

### 3. State Management & ViewModels
- **ViewModel**: Architecture components ViewModels are used to hold and manage UI-related data, surviving configuration changes.
  - `app/src/main/java/com/example/nempille/ui/auth/AuthViewModel.kt`
  - `app/src/main/java/com/example/nempille/ui/screens/patient/PatientMedicationListViewModel.kt`
- **Coroutines & `viewModelScope`**: Asynchronous operations (like saving data) are launched in `viewModelScope`, tying them to the ViewModel's lifecycle.
  - `completeSetup()` function in `app/src/main/java/com/example/nempille/ui/screens/setup/SetupViewModel.kt`
- **Dispatchers**: `Dispatchers.IO` is used for network calls to avoid blocking the main thread.
  - `showTestMedicationReminder()` in `app/src/main/java/com/example/nempille/ui/screens/notifications/NotificationHelper.kt`

### 4. Dependency Injection (DI)
- **Hilt Setup**: The `Application` class is annotated with `@HiltAndroidApp` and `MainActivity` uses `@AndroidEntryPoint`.
  - `app/src/main/java/com/example/nempille/NemPilleApp.kt`
  - `app/src/main/java/com/example/nempille/MainActivity.kt`
- **Constructor Injection**: Dependencies like repositories and use cases are injected directly into ViewModel constructors.
  - `app/src/main/java/com/example/nempille/ui/screens/setup/SetupViewModel.kt`
  - `app/src/main/java/com/example/nempille/ui/screens/patient/AddPatientViewModel.kt`

### 5. Local Storage (Database & Preferences)
- **Room Database**: The app's database is defined with `@Database` and provides DAOs.
  - **Database**: `app/src/main/java/com/example/nempille/data/local/database/AppDatabase.kt`
  - **Entity**: `app/src/main/java/com/example/nempille/data/local/entity/MedicationEntity.kt`
  - **DAO**: `app/src/main/java/com/example/nempille/data/local/dao/MedicationDao.kt`
- **Room with Flow**: DAOs return `Flow` so the UI can reactively update when database content changes.
  - `getMedicationsForUser(...)` in `app/src/main/java/com/example/nempille/data/local/dao/MedicationDao.kt`
- **DataStore**: Used as a modern replacement for `SharedPreferences` to asynchronously save the user's login session.
  - `app/src/main/java/com/example/nempille/data/local/datastore/AuthDataStore.kt`

### 6. Networking & API
- **Retrofit**: Used to define the network API for communicating with the Arduino device.
  - `app/src/main/java/com/example/nempille/data/RestApi.kt`
- **JSON Parsing**: Moshi is used with Retrofit for JSON serialization and deserialization.
  - `RetrofitInstance` in `app/src/main/java/com/example/nempille/data/RestApi.kt`
- **Repository Pattern (Local)**: The Repository pattern abstracts data sources from the rest of the app.
  - `app/src/main/java/com/example/nempille/data/repository/AuthenticationRepositoryImpl.kt`
  - `app/src/main/java/com/example/nempille/domain/repository/MedicationRepository.kt`

### 7. Background Processing & Alarms
- **AlarmManager**: Used to schedule precise, time-based medication reminders that can wake the device.
  - `app/src/main/java/com/example/nempille/ui/screens/notifications/MedicationReminderScheduler.kt`

### 8. Broadcasts, Sensors, and Permissions
- **Permissions Declaration**: Dangerous permissions for notifications and Bluetooth are declared in the manifest.
  - `app/src/main/AndroidManifest.xml`
- **BroadcastReceivers**: A receiver listens for scheduled alarms from `AlarmManager` and triggers a local notification.
  - `app/src/main/java/com/example/nempille/notifications/MedicationReminderReceiver.kt`

---

## Concept Definitions & Explanations

### Jetpack Compose UI & Architecture
*   **Single Activity Architecture**: A modern app structure where you have one main `Activity` that hosts multiple `Composable` screens. Navigation is handled by a `NavController`, which swaps Composables in and out. This is simpler and more efficient than managing multiple Activities.
*   **Unidirectional Data Flow (UDF)**: A design pattern where data flows in a single direction. The **UI** sends **events** up to the **ViewModel**, the ViewModel processes the event and updates its **state**, and the new state flows down to the UI to be rendered. This makes the app more predictable and easier to debug.
*   **State Hoisting**: The practice of moving state up from a Composable to its caller. This makes Composables more reusable and testable. A "stateless" Composable receives its state from above and calls lambdas (`onClick: () -> Unit`) to notify its parent of events.

### State Management & ViewModels
*   **ViewModel**: A class designed to store and manage UI-related data in a lifecycle-conscious way. A ViewModel survives configuration changes like screen rotations, so the data isn't lost.
*   **Coroutines & `viewModelScope`**: Coroutines are used for asynchronous programming. `viewModelScope` is a special `CoroutineScope` that is automatically cancelled when the ViewModel is destroyed, preventing memory leaks from long-running tasks.

### Dependency Injection (DI)
*   **Hilt**: A dependency injection library for Android that simplifies DI by automating much of the boilerplate code. It uses annotations to define and inject dependencies.
*   **Constructor Injection**: The best way to provide dependencies to a class. By declaring dependencies in the constructor, the class is explicit about what it needs to function.

### Local Storage
*   **Room**: A persistence library that provides an abstraction layer over SQLite. It allows you to write simple, annotated data classes (`@Entity`) and interfaces (`@Dao`) and let Room handle the SQL queries.
*   **Flow**: A type from Kotlin Coroutines for asynchronously handling a stream of data. Room uses `Flow` to allow the UI to observe database queries, automatically receiving updates when the data changes.
*   **DataStore**: The modern replacement for `SharedPreferences`. It provides a safe and asynchronous way to store simple key-value pairs using coroutines and Flow.

---

## Examples of Unimplemented Concepts

This section provides code examples for concepts not yet implemented in the project, which can be used for reference.

### 1. Material 3 Dynamic Colors

This would adapt the app's color scheme to the user's device wallpaper on Android 12+.

```kotlin
// file: app/src/main/java/com/example/nempille/ui/theme/Theme.kt

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun NemPilleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Check if device supports dynamic colors (Android 12+)
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme // Your custom dark theme
        else -> LightColorScheme      // Your custom light theme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

### 2. Hilt Modules with `@Provides`

This is the standard Hilt way to teach the dependency injection framework how to create instances of classes you don't own (like a Room database or a Retrofit client).

```kotlin
// file: app/src/main/java/com/example/nempille/di/AppModule.kt

import android.content.Context
import androidx.room.Room
import com.example.nempille.data.RestApi
import com.example.nempille.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nempille_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideRestApi(): RestApi {
         return Retrofit.Builder()
            .baseUrl("http://172.20.10.2/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RestApi::class.java)
    }
}
```

### 3. Repository Pattern (Single Source of Truth for Network)

This pattern ensures the UI always reads from the local database (Room), while the repository handles fetching data from the network and saving it to the database.

```kotlin
// file: app/src/main/java/com/example/nempille/data/repository/MedicationRepositoryImpl.kt

class MedicationRepositoryImpl @Inject constructor(
    private val api: RestApi,
    private val dao: MedicationDao
) : MedicationRepository {

    // The UI reads from this Flow, which is backed by the local database
    override fun getMedicationsForUser(userId: Int): Flow<List<Medication>> {
        return dao.getMedicationsForUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    // Function to refresh data from the network
    suspend fun refreshMedications(userId: Int) {
        try {
            // 1. Fetch from network API
            val networkMedications = api.getMedicationsFromApi(userId)
            // 2. Convert DTOs to local database entities
            val entities = networkMedications.map { it.toEntity() }
            // 3. Save to local database (which automatically updates the Flow for the UI)
            dao.insertAll(entities)
        } catch (e: Exception) {
            // Handle network errors
        }
    }
}
```

### 4. WorkManager for Background Tasks

`WorkManager` is the recommended solution for guaranteed, deferrable background work.

```kotlin
// file: app/src/main/java/com/example/nempille/workers/SyncDataWorker.kt

class SyncDataWorker(
    appContext: Context,
    workerParams: WorkerParameters
): CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // ... your background work, like calling a repository to sync data ...
            Log.d("SyncDataWorker", "Work finished successfully!")
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncDataWorker", "Work failed", e)
            Result.failure()
        }
    }
}

// How to schedule it from a ViewModel or UseCase
fun scheduleSync(context: Context) {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val syncRequest = OneTimeWorkRequestBuilder<SyncDataWorker>()
        .setConstraints(constraints)
        .build()

    WorkManager.getInstance(context).enqueue(syncRequest)
}
```

### 5. Rescheduling Alarms on Boot

To ensure medication reminders are not lost when the device restarts, a `BroadcastReceiver` must listen for `ACTION_BOOT_COMPLETED`.

```kotlin
// file: app/src/main/java/com/example/nempille/notifications/BootCompletedReceiver.kt

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Device rebooted, re-scheduling alarms...")
            // Here you would re-schedule all alarms by reading from your repository
            // and calling your MedicationReminderScheduler for each one.
        }
    }
}

// And in AndroidManifest.xml:
/*
<receiver
    android:name=".notifications.BootCompletedReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
*/
```

### 6. Runtime Permission Requests

For "dangerous" permissions like `POST_NOTIFICATIONS` on Android 13+, you must request permission from the user at runtime.

```kotlin
// file: app/src/main/java/com/example/nempille/ui/screens/notifications/NotificationsScreen.kt

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

@Composable
fun NotificationsScreen() { // ...
    val context = LocalContext.current

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) { /* Permission granted */ } else { /* Permission denied */ }
        }
    )

    Button(onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission already granted
                }
                else -> {
                    // Launch the permission request popup
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else { /* No permission needed for older versions */ }
    }) {
        Text("Show Test Notification")
    }
}
```

### 7. Firebase Cloud Messaging (FCM)

To receive push notifications from a server, you need to implement `FirebaseMessagingService`.

```kotlin
// file: app/src/main/java/com/example/nempille/notifications/MyFirebaseMessagingService.kt

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // Called when a new FCM token is generated.
    // Send this token to your server.
    override fun onNewToken(token: String) {
        Log.d("FCM", "New token: $token")
    }

    // Called when a message is received while the app is in the foreground.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle Notification payloads
        remoteMessage.notification?.let {
             Log.d("FCM", "Notification Message Body: ${it.body}")
        }

        // Handle Data payloads
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FCM", "Data Payload: " + remoteMessage.data)
        }
    }
}
```

### 8. Multiplatform (KMP & Flutter) - Weather App Example

#### A. Kotlin Multiplatform (KMP)

KMP allows you to share business logic between Android and iOS while writing platform-specific UI.

*   **KMP `expect`/`actual`**: This powerful mechanism allows you to define a common API in your shared code (`expect`) and provide platform-specific implementations (`actual`). It's perfect for accessing native features like GPS.

    ```kotlin
    // --- In commonMain module ---
    // We expect each platform to provide a way to get the location.
    expect class LocationProvider() {
        suspend fun getCurrentLocation(): Location
    }
    data class Location(val lat: Double, val long: Double)

    // --- In androidMain module ---
    // The actual Android implementation using Google's location services.
    actual class LocationProvider(@ApplicationContext private val context: Context) {
        actual suspend fun getCurrentLocation(): Location { 
            // ... Android-specific code to get GPS location ... 
        }
    }
    ```

*   **Shared Logic & Libraries**: The core logic, like fetching data from a weather API, resides in `commonMain` and is shared. 
    *   **Ktor**: A multiplatform networking library used instead of Retrofit.
    *   **Koin**: A multiplatform DI library used instead of Hilt.
    *   **SQLDelight**: A multiplatform database library that generates Kotlin APIs from SQL.

#### B. Flutter

Flutter is a UI toolkit for building natively compiled applications for mobile, web, and desktop from a single codebase written in Dart.

*   **Flutter Widgets**: The UI is built by composing widgets. 
    *   `StatelessWidget`: A widget that describes part of the user interface which can’t change over time (e.g., a simple weather icon).
    *   `StatefulWidget`: A widget that has mutable state. The main screen of a weather app would be a `StatefulWidget` to hold the current weather data and update the UI when new data is fetched.

*   **Flutter Channels**: Channels are used to communicate with platform-specific native code (Kotlin on Android, Swift on iOS). This is how you access native APIs like GPS.

    ```dart
    // --- In your Dart code (Flutter UI) ---
    import 'package:flutter/services.dart';

    class WeatherScreen extends StatefulWidget {
      // ...
    }

    class _WeatherScreenState extends State<WeatherScreen> {
      // Define the channel. The name must match the one in your native code.
      static const platform = MethodChannel('com.example.weather/location');
      String _location = 'Unknown location.';

      Future<void> _getLocation() async {
        try {
          // Invoke the 'getLocation' method on the native side.
          final String result = await platform.invokeMethod('getLocation');
          setState(() {
            _location = result;
          });
        } on PlatformException catch (e) {
          // Handle errors
        }
      }
      // ... build method ...
    }
    ```

    ```kotlin
    // --- In your MainActivity.kt (Android native side) ---
    import io.flutter.embedding.android.FlutterActivity
    import io.flutter.embedding.engine.FlutterEngine
    import io.flutter.plugin.common.MethodChannel

    class MainActivity: FlutterActivity() {
        private val CHANNEL = "com.example.weather/location"

        override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
            super.configureFlutterEngine(flutterEngine)
            MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
                call, result ->
                if (call.method == "getLocation") {
                    // ... code to get GPS location ...
                    val currentLocation = "New York, USA" // Placeholder
                    result.success(currentLocation)
                } else {
                    result.notImplemented()
                }
            }
        }
    }
    ```
