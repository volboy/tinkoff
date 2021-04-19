package com.volboy.course_project.presentation.streams

import android.util.Log
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loader
import com.volboy.course_project.App.Companion.resourceProvider
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.message_recycler_view.simple_items.ErrorItem
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StreamsPresenter : RxPresenter<StreamsView>(StreamsView::class.java) {
    private val appDatabase = App.appDatabase
    private var data = mutableListOf<ViewTyped>()

    fun getStreams() {
        loadStreamsFromDatabase()
    }

    fun getTopics(position: Int) {
        val clickedStream = data[position] as TitleUi
        clickedStream.imageId = R.drawable.ic_arrow_down
        data[position] = clickedStream
        loadRemoteTopics(clickedStream.uid.toInt(), position)
    }

    fun removeTopics(position: Int) {
        val newData = mutableListOf<ViewTyped>()
        data.forEach { item ->
            if ((item as TitleUi).streamsId != data[position].uid.toInt()) {
                if (item.uid == data[position].uid) {
                    item.imageId = R.drawable.ic_arrow_down
                }
                newData.add(item)
            }
        }
        view.hideData(newData)
        data = newData

    }

    private fun loadRemoteTopics(streamId: Int, position: Int) {
        val topic = loader.getTopicsOfStreams(streamId)
        topic.subscribe(
            { result ->
                val newData = mutableListOf<ViewTyped>()
                data.forEach { item ->
                    newData.add(item)
                    if (item.uid.toInt() == streamId) {
                        (item as TitleUi).imageId = R.drawable.ic_arrow_up
                        newData.addAll(result)
                    }
                }
                view.showData(newData)
                data = newData
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            {
                data.addAll(position, listOf(ErrorItem))
                writeLog(resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    private fun loadTopicFromDatabase(streamId: Int) {
        //TODO ("Сделать сохранение и загрузку топиков в БД")
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
                    if (viewTypedStreams.size == 0) {
                        view.showLoading("")
                    } else {
                        data = viewTypedStreams
                        view.showData(data)
                    }
                },
                { error ->
                    writeLog(resourceProvider.getString(R.string.msg_database_error) + error.message)
                },
                {
                    writeLog(resourceProvider.getString(R.string.msg_database_empty))
                })
            .disposeOnFinish()
        loadRemoteStreams()
    }

    private fun loadRemoteStreams() {
        view.showLoading("")
        val streams = loader.getRemoteStreams()
        streams.subscribe(
            { result ->
                data = result
                view.showData(data)
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(resourceProvider.getString(R.string.msg_network_error) + error.message)
            }
        )
            .disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(resourceProvider.getString(R.string.log_string), msg)
    }
}
