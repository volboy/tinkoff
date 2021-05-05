package com.volboy.courseproject.di

import android.content.Context
import com.volboy.courseproject.common.ResourceProvider
import dagger.Module
import dagger.Provides

@Module
class ResourceProviderModule(val context: Context) {

    @AppScope
    @Provides
    fun provideResourceProvider(): ResourceProvider {
        return ResourceProvider(context)
    }
}
