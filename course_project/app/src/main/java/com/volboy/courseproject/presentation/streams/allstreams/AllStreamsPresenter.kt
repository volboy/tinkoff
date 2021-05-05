package com.volboy.courseproject.presentation.streams.allstreams

import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.volboy.courseproject.App
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.database.AppDatabase
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.presentation.streams.mystreams.TitleUi
import com.volboy.courseproject.recyclerview.ViewTyped
import com.volboy.courseproject.recyclerview.simpleitems.EmptyView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AllStreamsPresenter : RxPresenter<AllStreamsView>(AllStreamsView::class.java) {
    private var data = mutableListOf<ViewTyped>()
    private var dataBaseError = false
    private lateinit var searchText: Observable<String>

    @Inject
    lateinit var loaderStreams: LoaderStreams

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var res: ResourceProvider

    init {
        App.component.injectLoaderStreams(this)
        App.component.injectDatabase(this)
        App.component.injectResourceProvider(this)
    }

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
            ).disposeOnFinish()
            if (text.isNullOrEmpty()) {
                view.showData(data)
            }
        }
    }

    private fun loadStreamsFromDatabase() {
        val streamsDao = appDatabase.streamsDao()
        streamsDao.getAllStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { streams -> loaderStreams.viewTypedStreams(streams) }
            .subscribe(
                { viewTypedStreams ->
                    writeLog(res.getString(R.string.msg_database_ok) + " , размер БД " + viewTypedStreams?.size?.toString())
                    if (viewTypedStreams.isEmpty()) {
                        view.showLoading("")
                        dataBaseError = true
                    } else {
                        data = viewTypedStreams as MutableList<ViewTyped>
                        view.showData(data)
                        dataBaseError = false
                    }
                },
                { error ->
                    writeLog(res.getString(R.string.msg_database_error) + error.message)
                    dataBaseError = true
                },
                {
                    writeLog(res.getString(R.string.msg_database_empty))
                    dataBaseError = true
                })
            .disposeOnFinish()
        loadRemoteStreams()
    }

    private fun loadRemoteStreams() {
        view.showLoading("")
        val streams = loaderStreams.getRemoteAllStreams()
        streams.subscribe(
            { result ->
                data = result as MutableList<ViewTyped>
                view.showData(data)
                writeLog(res.getString(R.string.msg_network_ok))
            },
            { error ->
                if (dataBaseError) {
                    view.showError(error.message)
                } else {
                    view.showData(data)
                }
                view.showError(error.message)
                writeLog(res.getString(R.string.msg_network_error) + error.message)
            }
        )
            .disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(res.getString(R.string.log_string), msg)
    }

    companion object {
        private const val TIME_SEARCH_DELAY = 500L
    }
}