package com.volboy.courseproject.presentation.profile

import android.util.Log
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.App.Companion.resourceProvider
import com.volboy.courseproject.R
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import javax.inject.Inject

class ProfilePresenter : RxPresenter<ProfileView>(ProfileView::class.java) {

    @Inject
    lateinit var loaderUsers: LoaderUsers

    init {
        component.injectLoaderUsers(this)
    }

    fun getOwnUser() {
        view.showLoading("")
        loadOwnUser()
    }

    private fun loadOwnUser() {
        val ownUser = loaderUsers.getOwnUser()
        ownUser.subscribe(
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

