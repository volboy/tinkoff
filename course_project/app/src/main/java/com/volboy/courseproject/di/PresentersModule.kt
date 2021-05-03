package com.volboy.courseproject.di

import com.volboy.courseproject.presentation.details.DetailsPresenter
import com.volboy.courseproject.presentation.messages.MessagesPresenter
import com.volboy.courseproject.presentation.profile.ProfilePresenter
import com.volboy.courseproject.presentation.streams.StreamsPresenter
import com.volboy.courseproject.presentation.users.UsersPresenter
import dagger.Module
import dagger.Provides

@Module
class PresentersModule {

    @AppScope
    @Provides
    fun provideStreamsPresenter(): StreamsPresenter {
        return StreamsPresenter()
    }

    @AppScope
    @Provides
    fun provideMessagePresenter(): MessagesPresenter {
        return MessagesPresenter()
    }

    @AppScope
    @Provides
    fun provideUsersPresenter(): UsersPresenter {
        return UsersPresenter()
    }

    @AppScope
    @Provides
    fun provideProfilePresenter(): ProfilePresenter {
        return ProfilePresenter()
    }

    @AppScope
    @Provides
    fun provideDetailsPresenter(): DetailsPresenter {
        return DetailsPresenter()
    }
}
