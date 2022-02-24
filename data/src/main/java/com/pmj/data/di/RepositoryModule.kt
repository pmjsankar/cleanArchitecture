package com.pmj.data.di

import com.pmj.data.repository.DiningRepositoryImpl
import com.pmj.domain.repository.DiningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    internal abstract fun bindDiningRepository(repository: DiningRepositoryImpl): DiningRepository
}