package com.example.ubc.di

import android.content.Context
import androidx.room.Room
import com.example.ubc.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context)
            : AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "app_db"
    ).build()
}