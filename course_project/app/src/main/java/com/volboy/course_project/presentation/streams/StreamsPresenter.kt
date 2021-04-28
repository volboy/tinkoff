package com.volboy.course_project.presentation.streams

import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.volboy.course_project.App
import com.volboy.course_project.App.Companion.loaderStreams
import com.volboy.course_project.App.Companion.resourceProvider
import com.volboy.course_project.R
import com.volboy.course_project.presentation.mvp.presenter.base.RxPresenter
import com.volboy.course_project.recyclerview.ViewTyped
import com.volboy.course_project.recyclerview.simple_items.EmptyView
import com.volboy.course_project.recyclerview.simple_items.ProgressItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class StreamsPresenter : RxPresenter<StreamsView>(StreamsView::class.java) {
    private val appDatabase = App.appDatabase
    private var data = mutableListOf<ViewTyped>()
    private var dataBaseError = false
    private lateinit var searchText: Observable<String>

    fun getStreams() {
        loadStreamsFromDatabase()
    }

    fun setSearchObservable(searchEdit: EditText) {
        searchEdit.addTextChangedListener { text ->
            searchText = Observable.create { emitter ->
                emitter.onNext(text.toString())
            }
            searchText
                .filter { inputText -> inputText.isNotEmpty() && inputText[0] != ' ' }
                .distinctUntilChanged()
                .debounce(TIME_SEARCH_DELAY, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())

            searchText.subscribe(
                { inputText ->
                    val filteredStreams = data.filter { stream ->
                        val item = stream as TitleUi
                        item.title.contains(inputText, ignoreCase = true)
                    }
                    if (filteredStreams.isEmpty()) {
                        view.showData(listOf(EmptyView))
                    } else {
                        view.showData(filteredStreams)
                    }
                },
                { error -> view.showData(data) }
            )
            if (text.isNullOrEmpty()) {
                view.showData(data)
            }
        }
    }

    fun getTopics(position: Int) {
        val clickedStream = data[position] as TitleUi
        clickedStream.imageId = R.drawable.ic_arrow_down
        data[position] = clickedStream
        loadRemoteTopics(clickedStream.uid.toInt())
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

    private fun loadRemoteTopics(streamId: Int) {
        val topic = loaderStreams.getTopicsOfStreams(streamId)
        val stream = data.firstOrNull { item -> item.uid == streamId.toString() }
        val streamIndex = if (stream != null) {
            data.indexOf(stream)
        } else {
            0
        }
        data.add(streamIndex + 1, ProgressItem)
        view.updateData(data, streamIndex + 1)
        topic.subscribe(
            { result ->
                val newData = mutableListOf<ViewTyped>()
                data.forEach { item ->
                    newData.add(item)
                    if (item.uid == streamId.toString()) {
                        (item as TitleUi).imageId = R.drawable.ic_arrow_up
                        newData.addAll(result)
                    }
                }
                newData.remove(ProgressItem)
                view.showData(newData)
                data = newData
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            {
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
            .map { streams -> loaderStreams.viewTypedStreams(streams) }
            .subscribe(
                { viewTypedStreams ->
                    writeLog(resourceProvider.getString(R.string.msg_database_ok) + " , размер БД " + viewTypedStreams?.size?.toString())
                    if (viewTypedStreams.size == 0) {
                        view.showLoading("")
                        dataBaseError = true
                    } else {
                        data = viewTypedStreams
                        view.showData(data)
                        dataBaseError = false
                    }
                },
                { error ->
                    writeLog(resourceProvider.getString(R.string.msg_database_error) + error.message)
                    dataBaseError = true
                },
                {
                    writeLog(resourceProvider.getString(R.string.msg_database_empty))
                    dataBaseError = true
                })
            .disposeOnFinish()
        loadRemoteStreams()
    }

    private fun loadRemoteStreams() {
        view.showLoading("")
        val streams = loaderStreams.getRemoteStreams()
        streams.subscribe(
            { result ->
                data = result
                view.showData(data)
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                if (dataBaseError) {
                    view.showError(error.message)
                } else {
                    view.showData(data)
                }
                view.showError(error.message)
                writeLog(resourceProvider.getString(R.string.msg_network_error) + error.message)
            }
        )
            .disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(resourceProvider.getString(R.string.log_string), msg)
    }

    companion object {
        private const val TIME_SEARCH_DELAY = 500L
    }
}
