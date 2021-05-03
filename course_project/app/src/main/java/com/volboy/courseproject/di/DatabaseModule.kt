package com.volboy.courseproject.di

import android.content.Context
import androidx.room.Room
import com.volboy.courseproject.database.AppDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(val context: Context) {

    @AppScope
    @Provides
    fun provideDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "zulipAppDatabase"
        ).build()
    }
}