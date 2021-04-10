package com.volboy.course_project

import android.app.Application
import androidx.room.Room
import com.volboy.course_project.repository.AppDatabase
import internet.ZulipApi
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    lateinit var zulipApi: ZulipApi

    companion object {
        lateinit var instance: App
        lateinit var appDatabase: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "zulipAppDatabase")
            .build()
        initRetrofit()
    }

    private fun initRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Auth)
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://tfs-android-2021-spring.zulipchat.com/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        zulipApi = retrofit.create(ZulipApi::class.java)
    }

    private object Auth : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header(
                    "Authorization",
                    Credentials.basic("volboy@yandex.ru", "qCqk5Jt7Hlcm2jd6hBxKab9CRbT0TgC5")
                ).build()
            return chain.proceed(authenticatedRequest)
        }

    }
}