package com.example.nempille.di

import com.example.nempille.domain.repository.PatientCaregiverRepository
import com.example.nempille.domain.repository.UserRepository
import com.example.nempille.domain.usecase.CreatePatientAndAttachToCaregiverUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCreatePatientAndAttachToCaregiverUseCase(
        userRepository: UserRepository,
        patientCaregiverRepository: PatientCaregiverRepository
    ): CreatePatientAndAttachToCaregiverUseCase {
        return CreatePatientAndAttachToCaregiverUseCase(
            userRepository = userRepository,
            patientCaregiverRepository = patientCaregiverRepository
        )
    }
}