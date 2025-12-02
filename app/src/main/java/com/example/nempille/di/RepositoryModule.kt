package com.example.nempille.di

import com.example.nempille.data.local.dao.MedicationDao
import com.example.nempille.data.repository.MedicationRepositoryImpl
import com.example.nempille.domain.repository.MedicationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//module that binds interfaces (domain) to implement (data)
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    //provide singleton MedicationRepository
    @Provides
    @Singleton
    fun provideMedicationRepository(
        dao: MedicationDao
    ): MedicationRepository {
        //hilt already knows how to get MedicationDao (from DatabaseModule)
        return MedicationRepositoryImpl(dao)
    }
}