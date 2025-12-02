package com.example.nempille.di

import android.content.Context
import androidx.room.Room
import com.example.nempille.data.local.database.AppDatabase
import com.example.nempille.data.local.dao.MedicationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//this class tells Hilt HOW to create database-related objects
@Module //class defines bindings for DI
@InstallIn(SingletonComponent::class)//available app-wide (singleton scope)
object DatabaseModule{
    //provide a single instance of AppDatabase for the whole app
    @Provides
    @Singleton //only one db instance
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nempille_db" //name of the sqlite file
        ).build()
    }

    //Provide DAO. Hilt knows how to get AppDatabase from the fun above
    @Provides
    fun provideMedicationDao(
        database: AppDatabase
    ): MedicationDao {
        return database.medicationDao()
    }
}