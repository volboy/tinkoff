package com.volboy.course_project.presentation.streams

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.resourceProvider
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.Loader
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StreamsPresenter() : RxPresenter<StreamsView>(StreamsView::class.java) {
    private val loader = Loader()
    private val appDatabase = App.appDatabase
    private var mode = START_POINT
    private var data = mutableListOf<ViewTyped>()

    fun getStreams() {
        when (mode) {
            START_POINT -> loadStreamsFromDatabase()
            OK_DATABASE -> {
                view.showStreams(data)
                loadRemoteStreams()
            }
            EMPTY_DATABASE, ERROR_DATABASE -> {
                view.showLoading()
                loadRemoteStreams()
            }
            OK_INTERNET -> {
                view.showStreams(data)
            }
            ERROR_INTERNET -> {
                view.showError()
                mode = START_POINT
            }
        }
    }

    private fun loadStreamsFromDatabase() {
        val streamsDao = appDatabase.streamsDao()
        streamsDao.getAllStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { streams -> loader.viewTypedStreams(streams) }
            .subscribe(
                { viewTypedStreams ->
                    writeLog(resourceProvider.getString(R.string.msg_database_ok) + " , размер БД " + viewTypedStreams?.size?.toString())
                    mode = if (viewTypedStreams.size == 0) {
                        EMPTY_DATABASE
                    } else {
                        data = viewTypedStreams
                        OK_DATABASE
                    }
                    getStreams()
                },
                { error ->
                    writeLog(resourceProvider.getString(R.string.msg_database_error) + error.message)
                    mode = ERROR_DATABASE
                    getStreams()
                },
                {
                    writeLog(resourceProvider.getString(R.string.msg_database_empty))
                    mode = EMPTY_DATABASE
                    getStreams()
                })
            .disposeOnFinish()

    }

    private fun loadRemoteStreams() {
        val streams = loader.getRemoteStreams()
        streams.subscribe(
            { result ->
                data = result
                mode = OK_INTERNET
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
                getStreams()
            },
            { error ->
                mode = ERROR_INTERNET
                writeLog(resourceProvider.getString(R.string.msg_network_error) + error.message)
                getStreams()
            }
        )
            .disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(resourceProvider.getString(R.string.log_string), msg)
    }

    private companion object {
        const val START_POINT = -1
        const val EMPTY_DATABASE = 1
        const val ERROR_DATABASE = 2
        const val OK_DATABASE = 3
        const val ERROR_INTERNET = 4
        const val OK_INTERNET = 5
    }
}
