package com.volboy.courseproject

import android.app.Application
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.volboy.courseproject.api.ZulipApi
import com.volboy.courseproject.di.AppComponent
import com.volboy.courseproject.di.DaggerAppComponent
import com.volboy.courseproject.di.DatabaseModule
import com.volboy.courseproject.di.LoadersModule
import com.volboy.courseproject.presentation.details.DetailsPresenter
import com.volboy.courseproject.presentation.messages.MessagesPresenter
import com.volboy.courseproject.presentation.profile.ProfilePresenter
import com.volboy.courseproject.presentation.streams.StreamsPresenter
import com.volboy.courseproject.presentation.users.UsersPresenter
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
        lateinit var resourceProvider: ResourceProvider
        lateinit var streamsPresenter: StreamsPresenter
        lateinit var usersPresenter: UsersPresenter
        lateinit var profilePresenter: ProfilePresenter
        lateinit var detailsPresenter: DetailsPresenter
        lateinit var messagesPresenter: MessagesPresenter

        lateinit var component: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerAppComponent
            .builder()
            .databaseModule(DatabaseModule(applicationContext))
            .loadersModule(LoadersModule())
            .build()

        instance = this
        initRetrofit()
        resourceProvider = ResourceProvider()
        streamsPresenter = StreamsPresenter()
        usersPresenter = UsersPresenter()
        profilePresenter = ProfilePresenter()
        detailsPresenter = DetailsPresenter()
        messagesPresenter = MessagesPresenter()
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

    inner class ResourceProvider {
        fun getString(@StringRes resId: Int): String = applicationContext.getString(resId)
        fun getColor(@ColorRes resId: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            applicationContext.getColor(resId)
        } else {
            applicationContext.resources.getColor(resId)
        }
    }
}
