package com.pmj.cleanarchitecture.di

import android.content.Context
import com.pmj.cleanarchitecture.datastore.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserPreferencesModule {

    @Singleton
    @Provides
    fun provideUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)
}