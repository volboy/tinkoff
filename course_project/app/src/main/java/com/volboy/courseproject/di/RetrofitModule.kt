package com.volboy.courseproject.di

import com.volboy.courseproject.api.ZulipApi
import dagger.Module
import dagger.Provides
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule {

    @AppScope
    @Provides
    fun provideRetrofit(): ZulipApi {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(Auth)
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(APP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(ZulipApi::class.java)
    }

    private object Auth : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header(
                    APP_AUTH_REQ,
                    Credentials.basic(APP_AUTH_LOGIN, APP_AUTH_KEY)
                ).build()
            return chain.proceed(authenticatedRequest)
        }
    }

    private companion object {
        const val APP_BASE_URL = "TODO создать свою группу в Zulip"
        const val APP_AUTH_REQ = "Authorization"
        const val APP_AUTH_LOGIN = "TODO зарегистрироваться как пользователь "
        const val APP_AUTH_KEY = "TODO сгенерить ключ https://zulip.com/api/api-keys"
    }
}
