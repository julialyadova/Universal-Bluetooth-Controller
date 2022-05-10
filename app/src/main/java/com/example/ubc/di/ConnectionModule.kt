package com.example.ubc.di

import com.example.ubc.connection.ConnectionService
import com.example.ubc.test.TestConnectionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConnectionModule {
    @Provides
    @Singleton
    fun provideConnectionService(): ConnectionService = TestConnectionService()
}