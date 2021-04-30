package com.volboy.courseproject.presentation.users

import android.util.Log
import com.volboy.courseproject.App.Companion.loaderUsers
import com.volboy.courseproject.App.Companion.resourceProvider
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter

class UsersPresenter : RxPresenter<UsersView>(UsersView::class.java) {

    fun getUsers() {
        view.showLoading("")
        loadRemoteUsers()
    }

    private fun loadRemoteUsers() {
        val users = loaderUsers.getRemoteUsers()
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