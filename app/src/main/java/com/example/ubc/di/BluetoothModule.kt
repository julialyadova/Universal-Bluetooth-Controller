package com.example.ubc.di

import android.bluetooth.BluetoothAdapter
import com.example.ubc.bluetooth.BluetoothService
import com.example.ubc.test.TestBluetoothService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class BluetoothModule {
    @Provides
    fun provideBluetoothService(): BluetoothService = BluetoothService()

    @Provides
    fun provideTestBluetoothService(): TestBluetoothService = TestBluetoothService()

    @Provides
    fun provideBluetoothAdapter(): BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
}