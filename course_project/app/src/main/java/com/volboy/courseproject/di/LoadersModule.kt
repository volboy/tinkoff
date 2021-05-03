package com.volboy.courseproject.di

import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.model.LoaderUsers
import dagger.Module
import dagger.Provides

@Module
class LoadersModule {

    @Provides
    @AppScope
    fun provideLoaderStreams(): LoaderStreams {
        return LoaderStreams()
    }

    @Provides
    @AppScope
    fun provideLoaderMessage(): LoaderMessage {
        return LoaderMessage()
    }

    @Provides
    @AppScope
    fun provideLoaderUsers(): LoaderUsers {
        return LoaderUsers()
    }
}