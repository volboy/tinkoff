package com.volboy.courseproject.di

import com.volboy.courseproject.MainPresenter
import com.volboy.courseproject.presentation.addstream.AddStreamPresenter
import com.volboy.courseproject.presentation.details.DetailsPresenter
import com.volboy.courseproject.presentation.messages.MessagesPresenter
import com.volboy.courseproject.presentation.msgstream.MessagesOfStreamsPresenter
import com.volboy.courseproject.presentation.profile.ProfilePresenter
import com.volboy.courseproject.presentation.streams.allstreams.AllStreamsPresenter
import com.volboy.courseproject.presentation.streams.mystreams.SubStreamsPresenter
import com.volboy.courseproject.presentation.users.UsersPresenter
import dagger.Module
import dagger.Provides

@Module
class PresentersModule {

    @AppScope
    @Provides
    fun provideStreamsPresenter(): SubStreamsPresenter {
        return SubStreamsPresenter()
    }

    @AppScope
    @Provides
    fun provideAllStreamsPresenter(): AllStreamsPresenter {
        return AllStreamsPresenter()
    }

    @AppScope
    @Provides
    fun provideMessagePresenter(): MessagesPresenter {
        return MessagesPresenter()
    }

    @AppScope
    @Provides
    fun provideAllMessagePresenter(): MessagesOfStreamsPresenter {
        return MessagesOfStreamsPresenter()
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

    @AppScope
    @Provides
    fun provideAddStreamPresenter(): AddStreamPresenter {
        return AddStreamPresenter()
    }

    @AppScope
    @Provides
    fun provideMainPresenter(): MainPresenter {
        return MainPresenter()
    }
}
