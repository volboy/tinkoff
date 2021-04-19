package com.volboy.course_project.presentation.mvp.presenter

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.presentation.mvp.presenter.base.Presenter

class MvpHelper<View, P: Presenter<View>>(
    private val callback: MvpViewCallback<View, P>
) {

    private lateinit var presenter: Presenter<View>

    fun create() {
        presenter = callback.getPresenter()
        presenter.attachView(callback.getMvpView())
        Log.i(App.resourceProvider.getString(R.string.log_string), "attach")
    }

    fun destroy(isFinishing: Boolean) {
        presenter.detachView(isFinishing)
        Log.i(App.resourceProvider.getString(R.string.log_string), "detach")
    }
}