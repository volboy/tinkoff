package com.volboy.courseproject.di

import com.volboy.courseproject.MainActivity
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.presentation.details.DetailsPresenter
import com.volboy.courseproject.presentation.messages.MessagesPresenter
import com.volboy.courseproject.presentation.profile.ProfilePresenter
import com.volboy.courseproject.presentation.streams.StreamsPresenter
import com.volboy.courseproject.presentation.users.UsersPresenter
import dagger.Component

@Component(modules = [LoadersModule::class, DatabaseModule::class])
@AppScope
interface AppComponent {

    fun injectLoaderStreams(streamsPresenter: StreamsPresenter)

    fun injectLoaderMessages(messagesPresenter: MessagesPresenter)

    fun injectLoaderUsers(usersPresenter: UsersPresenter)
    fun injectLoaderUsers(mainActivity: MainActivity)
    fun injectLoaderUsers(detailsPresenter: DetailsPresenter)
    fun injectLoaderUsers(profilePresenter: ProfilePresenter)

    fun injectDatabase(streamsPresenter: StreamsPresenter)
    fun injectDatabase(messagesPresenter: MessagesPresenter)
    fun injectDatabase(loaderStreams: LoaderStreams)
    fun injectDatabase(loaderMessage: LoaderMessage)
}