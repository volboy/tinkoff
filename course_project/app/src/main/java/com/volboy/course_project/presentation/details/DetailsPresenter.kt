package com.volboy.course_project.presentation.details

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter

class DetailsPresenter : RxPresenter<DetailsView>(DetailsView::class.java) {

    fun getUserStatus(id: Int) {
        view.showLoading("")
        val presence = App.loaderUsers.getUserStatus(id)
        presence.subscribe(
            { result ->
                view.showStatus(result.presence.aggregated.status)
                writeLog(App.resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(App.resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(App.resourceProvider.getString(R.string.log_string), msg)
    }
}