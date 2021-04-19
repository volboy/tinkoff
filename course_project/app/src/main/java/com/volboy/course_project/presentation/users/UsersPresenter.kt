package com.volboy.course_project.presentation.users

import android.util.Log
import com.volboy.course_project.App.Companion.loader
import com.volboy.course_project.App.Companion.resourceProvider
import com.volboy.course_project.R
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter

class UsersPresenter : RxPresenter<UsersView>(UsersView::class.java) {

    fun getUsers() {
        view.showLoading("")
        loadRemoteUsers()
    }

    private fun loadRemoteUsers() {
        val users = loader.getRemoteUsers()
        users.subscribe(
            { result ->
                view.showData(result)
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(resourceProvider.getString(R.string.log_string), msg)
    }
}