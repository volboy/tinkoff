package com.volboy.courseproject.di

import com.volboy.courseproject.MainActivity
import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.details.DetailsPresenter
import com.volboy.courseproject.presentation.details.MvpDetailsFragment
import com.volboy.courseproject.presentation.messages.MessagesPresenter
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment
import com.volboy.courseproject.presentation.profile.MvpProfileFragment
import com.volboy.courseproject.presentation.profile.ProfilePresenter
import com.volboy.courseproject.presentation.streams.MvpStreamsFragment
import com.volboy.courseproject.presentation.streams.MvpSubscribedFragment
import com.volboy.courseproject.presentation.streams.StreamsPresenter
import com.volboy.courseproject.presentation.users.MvpUsersFragment
import com.volboy.courseproject.presentation.users.UsersPresenter
import dagger.Component

@Component(
    modules = [
        LoadersModule::class,
        DatabaseModule::class,
        RetrofitModule::class,
        PresentersModule::class,
        ResourceProviderModule::class
    ]
)
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

    fun injectRetrofit(loaderMessage: LoaderMessage)
    fun injectRetrofit(loaderStreams: LoaderStreams)
    fun injectRetrofit(loaderUsers: LoaderUsers)

    fun injectDetailPresenter(mvpDetailsFragment: MvpDetailsFragment)

    fun injectMessagePresenter(mvpMessagesFragment: MvpMessagesFragment)

    fun injectProfilePresenter(mvpProfileFragment: MvpProfileFragment)

    fun injectStreamsPresenter(mvpStreamsFragment: MvpStreamsFragment)
    fun injectStreamsPresenter(mvpSubscribedFragment: MvpSubscribedFragment)

    fun injectUsersPresenter(mvpUsersFragment: MvpUsersFragment)

    fun injectResourceProvider(loaderMessage: LoaderMessage)
    fun injectResourceProvider(detailsPresenter: DetailsPresenter)
    fun injectResourceProvider(mvpDetailsFragment: MvpDetailsFragment)
    fun injectResourceProvider(messagesPresenter: MessagesPresenter)
    fun injectResourceProvider(profilePresenter: ProfilePresenter)
    fun injectResourceProvider(streamsPresenter: StreamsPresenter)
    fun injectResourceProvider(usersPresenter: UsersPresenter)

}