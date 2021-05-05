package com.volboy.courseproject.presentation.streams.mystreams

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.recyclerview.ViewTyped
import com.volboy.courseproject.recyclerview.simpleitems.EmptyView
import com.volboy.courseproject.recyclerview.simpleitems.ProgressItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SubStreamsPresenter : RxPresenter<SubStreamsView>(SubStreamsView::class.java) {
    private var data = mutableListOf<ViewTyped>()
    private var dataFromDatabase = mutableListOf<ViewTyped>()
    private var dataBaseError = false

    private lateinit var searchText: Observable<String>

    @Inject
    lateinit var loaderStreams: LoaderStreams

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var res: ResourceProvider

    init {
        component.injectLoaderStreams(this)
        component.injectDatabase(this)
        component.injectResourceProvider(this)
    }

    fun getStreams() {
        if (dataFromDatabase.isNotEmpty()) {
            view.showData(dataFromDatabase)
            loadRemoteStreams()
        } else {
            loadStreamsFromDatabase()
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
            ).disposeOnFinish()
            if (text.isNullOrEmpty()) {
                view.showData(data)
            }
        }
    }

    private fun loadStreamsFromDatabase() {
        loaderStreams.getSubStreamsFromDB().subscribe(
            { subStreams ->
                if (subStreams.isNotEmpty()) {
                    data = subStreams as MutableList<ViewTyped>
                    dataFromDatabase = subStreams
                    view.showData(data)
                }
            },
            { error ->
                view.showMessage(res.getString(R.string.msg_database_error) + error.message)
            },
            {
                view.showMessage(res.getString(R.string.msg_database_empty))
            })
            .disposeOnFinish()
        loadRemoteStreams()
    }

    private fun loadRemoteStreams() {
        view.showLoading("")
        loaderStreams.getRemoteSubStreams().subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showData(data)
            },
            { error ->
                if (dataFromDatabase.isEmpty()) {
                    view.showError(error.message)
                } else {
                    view.showData(data)
                }
            }
        ).disposeOnFinish()
    }

    private fun loadTopicFromDatabase(streamId: Int) {
        //TODO ("Сделать сохранение и загрузку топиков в БД")
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
                (stream as TitleUi).imageId = R.drawable.ic_arrow_up
                val newData = ArrayList(data)
                newData.addAll(streamIndex + 1, result)
                newData.remove(ProgressItem)
                view.showData(newData)
                data = newData
            },
            {
                view.showMessage(res.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    companion object {
        private const val TIME_SEARCH_DELAY = 500L
    }
}
