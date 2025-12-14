package com.example.nempille.di

import android.content.Context
import com.example.nempille.ui.screens.notifications.MedicationReminderScheduler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DI module that provides notification-related helpers.
 */
@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Provides
    @Singleton
    fun provideMedicationReminderScheduler(
        @ApplicationContext context: Context
    ): MedicationReminderScheduler {
        return MedicationReminderScheduler(context)
    }
}