package com.volboy.course_project.presentation.streams

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.simple_items.ProgressItem
import com.volboy.course_project.model.Loader
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StreamsPresenter(streamsView: StreamsView) : RxPresenter<StreamsView>(StreamsView::class.java) {
    private val loader = Loader()
    private val appDatabase = App.appDatabase

    fun loadStreams() {
        val streamsDao = appDatabase.streamsDao()
        val disposable = streamsDao.getAllStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { streams -> loader.viewTypedStreams(streams) }
            .subscribe(
                { viewTypedStreams ->
                    showSnackbar(resources.getString(R.string.msg_database_ok) + " , размер БД " + viewTypedStreams?.size?.toString())
                    if (viewTypedStreams.size == 0) {
                        commonAdapter.items = listOf(ProgressItem)
                    } else {
                        commonAdapter.items = viewTypedStreams
                    }
                },
                { error -> showSnackbar(resources.getString(R.string.msg_database_error) + error.message) },
                {
                    showSnackbar(resources.getString(R.string.msg_database_empty))
                    commonAdapter.items = listOf(ProgressItem)
                })
        view.showLoading()

    }

    fun loadTopics() {

    }
    private fun showSnackbar(msg: String) {
        Log.i(resources.getString(R.string.log_string), msg)
    }
}