package com.example.ubc.di

import com.example.ubc.bluetooth.BluetoothService
import com.example.ubc.connection.ConnectionService
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
    fun provideConnectionService(): ConnectionService = BluetoothService()
}