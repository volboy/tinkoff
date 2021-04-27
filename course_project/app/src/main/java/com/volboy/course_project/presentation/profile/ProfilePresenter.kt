package com.volboy.course_project.presentation.profile

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loaderUsers
import com.volboy.course_project.App.Companion.resourceProvider
import com.volboy.course_project.R
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter

class ProfilePresenter : RxPresenter<ProfileView>(ProfileView::class.java) {

    fun getOwnUser() {
        view.showLoading("")
        loadOwnUser()
    }

    private fun loadOwnUser() {
        val ownUser = loaderUsers.getOwnUser()
        val disposableMessages = ownUser.subscribe(
            { result ->
                view.showData(result)
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(resourceProvider.getString(R.string.msg_network_error))
            }
        )
    }

    private fun writeLog(msg: String) {
        Log.i(App.resourceProvider.getString(R.string.log_string), msg)
    }
}

