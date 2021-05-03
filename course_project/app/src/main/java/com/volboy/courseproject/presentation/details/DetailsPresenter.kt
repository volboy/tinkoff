package com.volboy.courseproject.presentation.details

import android.util.Log
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import javax.inject.Inject

class DetailsPresenter : RxPresenter<DetailsView>(DetailsView::class.java) {

    @Inject
    lateinit var loaderUsers: LoaderUsers

    @Inject
    lateinit var res: ResourceProvider

    init {
        component.injectLoaderUsers(this)
        component.injectResourceProvider(this)
    }

    fun getUserStatus(id: Int) {
        view.showLoading("")
        val presence = loaderUsers.getUserStatus(id)
        presence.subscribe(
            { result ->
                view.showStatus(result.presence.aggregated.status)
                writeLog(res.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(res.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(res.getString(R.string.log_string), msg)
    }
}