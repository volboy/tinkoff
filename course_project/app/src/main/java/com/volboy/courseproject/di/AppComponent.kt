package com.volboy.courseproject.di

import com.volboy.courseproject.MainActivity
import com.volboy.courseproject.MainPresenter
import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.model.MessageMapper
import com.volboy.courseproject.presentation.addstream.AddStreamPresenter
import com.volboy.courseproject.presentation.addstream.MvpAddStreamFragment
import com.volboy.courseproject.presentation.details.DetailsPresenter
import com.volboy.courseproject.presentation.details.MvpDetailsFragment
import com.volboy.courseproject.presentation.messages.MessagesPresenter
import com.volboy.courseproject.presentation.messages.MvpMessagesFragment
import com.volboy.courseproject.presentation.msgstream.MessagesOfStream
import com.volboy.courseproject.presentation.msgstream.MessagesOfStreamsPresenter
import com.volboy.courseproject.presentation.profile.MvpProfileFragment
import com.volboy.courseproject.presentation.profile.ProfilePresenter
import com.volboy.courseproject.presentation.streams.allstreams.AllStreamsPresenter
import com.volboy.courseproject.presentation.streams.allstreams.MvpStreamsFragment
import com.volboy.courseproject.presentation.streams.mystreams.MvpSubscribedFragment
import com.volboy.courseproject.presentation.streams.mystreams.SubStreamsPresenter
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

    fun injectLoaderStreams(subStreamsPresenter: SubStreamsPresenter)
    fun injectLoaderStreams(allStreamsPresenter: AllStreamsPresenter)
    fun injectLoaderStreams(addStreamPresenter: AddStreamPresenter)

    fun injectLoaderMessages(messagesPresenter: MessagesPresenter)
    fun injectLoaderMessages(messagesOfStreamsPresenter: MessagesOfStreamsPresenter)

    fun injectLoaderUsers(usersPresenter: UsersPresenter)
    fun injectLoaderUsers(mainPresenter: MainPresenter)
    fun injectLoaderUsers(detailsPresenter: DetailsPresenter)
    fun injectLoaderUsers(profilePresenter: ProfilePresenter)

    fun injectDatabase(allStreamsPresenter: AllStreamsPresenter)
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

    fun injectMainPresenter(mainActivity: MainActivity)

    fun injectAddStreamPresenter(mvpAddStreamFragment: MvpAddStreamFragment)

    fun injectAllMessagesPresenter(messagesOfStream: MessagesOfStream)

    fun injectResourceProvider(messageMapper: MessageMapper)
    fun injectResourceProvider(detailsPresenter: DetailsPresenter)
    fun injectResourceProvider(mvpDetailsFragment: MvpDetailsFragment)
    fun injectResourceProvider(messagesPresenter: MessagesPresenter)
    fun injectResourceProvider(profilePresenter: ProfilePresenter)
    fun injectResourceProvider(subStreamsPresenter: SubStreamsPresenter)
    fun injectResourceProvider(allStreamsPresenter: AllStreamsPresenter)
    fun injectResourceProvider(usersPresenter: UsersPresenter)
    fun injectResourceProvider(addStreamPresenter: AddStreamPresenter)
    fun injectResourceProvider(messagesOfStreamsPresenter: MessagesOfStreamsPresenter)
    fun injectResourceProvider(mainPresenter: MainPresenter)

}