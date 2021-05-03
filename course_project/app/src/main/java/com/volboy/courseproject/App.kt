package com.volboy.courseproject

import android.app.Application
import com.volboy.courseproject.di.*

class App : Application() {

    companion object {
        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
            .builder()
            .databaseModule(DatabaseModule(applicationContext))
            .loadersModule(LoadersModule())
            .retrofitModule(RetrofitModule())
            .presentersModule(PresentersModule())
            .resourceProviderModule(ResourceProviderModule(applicationContext))
            .build()
    }
}
