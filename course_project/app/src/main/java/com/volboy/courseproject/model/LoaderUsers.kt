package com.volboy.courseproject.model

import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.api.ZulipApi
import com.volboy.courseproject.presentation.users.PeopleUi
import com.volboy.courseproject.recyclerview.ViewTyped
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LoaderUsers {

    @Inject
    lateinit var zulipApi: ZulipApi

    init {
        component.injectRetrofit(this)
    }

    fun getRemoteUsers(): Single<MutableList<ViewTyped>> =
        zulipApi.getUsers()
            .subscribeOn(Schedulers.io())
            .map { response -> viewTypedUsers(response.members) }
            .observeOn(AndroidSchedulers.mainThread())


    fun getOwnUser(): Single<OwnUser> =
        zulipApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getUserStatus(id: Int): Single<StatusUserResponse> =
        zulipApi.getUserStatus(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    private fun viewTypedUsers(usersJSON: List<UserJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        usersJSON.forEach { user ->
            val uid = user.userId.toString()
            viewTypedList.add(
                PeopleUi(
                    user.fullName,
                    user.email,
                    user.avatar_url,
                    EMPTY_STATUS_STRING,
                    R.layout.item_people_list,
                    uid
                )
            )
        }
        return viewTypedList
    }

    private companion object {
        const val EMPTY_STATUS_STRING = ""
    }
}
