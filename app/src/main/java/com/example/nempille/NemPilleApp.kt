package com.example.nempille

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * CONCEPT: Hilt Setup (Dependency Injection)
 * 
 * How it works:
 * The @HiltAndroidApp annotation triggers Hilt's code generation. 
 * It creates a base class for your application that serves as the 
 * application-level dependency container. This is the entry point 
 * for Hilt in your app.
 */
@HiltAndroidApp
class NemPilleApp : Application()
