package com.volboy.course_project

import android.app.Application
import internet.ZulipService
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MessagerApp : Application() {
    private lateinit var instance: MessagerApp
    lateinit var zulipService: ZulipService

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRetrofit();
    }

    private fun getInstance(): MessagerApp {
        return instance
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
            .build();
        zulipService = retrofit.create(ZulipService::class.java)
    }
}

object Auth : Interceptor {
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
