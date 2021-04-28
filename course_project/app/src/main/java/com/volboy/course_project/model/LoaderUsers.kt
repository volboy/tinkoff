package com.volboy.course_project.model

import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.presentation.users.PeopleUi
import com.volboy.course_project.recyclerview.ViewTyped
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoaderUsers {

    fun getRemoteUsers(): Single<MutableList<ViewTyped>> {
        return App.instance.zulipApi.getUsers()
            .subscribeOn(Schedulers.io())
            .map { response -> viewTypedUsers(response.members) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getOwnUser(): Single<OwnUser> {
        return App.instance.zulipApi.getOwnUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserStatus(id: Int): Single<StatusUserResponse> {
        return App.instance.zulipApi.getUserStatus(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun viewTypedUsers(usersJSON: List<UserJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        usersJSON.forEach { user ->
            val uid = user.userId.toString()
            viewTypedList.add(
                PeopleUi(
                    user.fullName,
                    user.email,
                    user.avatar_url,
                    R.layout.item_people_list,
                    uid
                )
            )
        }
        return viewTypedList
    }
}