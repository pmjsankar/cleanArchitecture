package com.pmj.domain.di

import com.pmj.domain.usecase.DiningUseCase
import com.pmj.domain.usecase.DiningUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    @Singleton
    internal abstract fun bindDiningUseCase(useCaseImpl: DiningUseCaseImpl): DiningUseCase
}