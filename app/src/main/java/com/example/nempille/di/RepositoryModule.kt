package com.example.nempille.di

import com.example.nempille.data.local.dao.MedicationDao
import com.example.nempille.data.repository.MedicationRepositoryImpl
import com.example.nempille.data.repository.UserRepositoryImpl
import com.example.nempille.domain.repository.MedicationRepository
import com.example.nempille.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//module that binds interfaces (domain) to implement (data)
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    //BIND MedicationRepository interface → MedicationRepositoryImpl implementation
    @Binds
    @Singleton
    abstract fun bindMedicationRepository(
        impl: MedicationRepositoryImpl
    ): MedicationRepository

    // BIND UserRepository interface → UserRepositoryImpl implementation
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}