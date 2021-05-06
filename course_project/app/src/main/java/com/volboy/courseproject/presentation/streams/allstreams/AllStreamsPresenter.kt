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
    private lateinit var searchText: Observable<String>
    private var allStreams = mutableListOf<ViewTyped>()
    private var allStreamsDB = mutableListOf<ViewTyped>()
    private var subStreams = mutableListOf<ViewTyped>()

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
        loadSubStreamsFromDatabase()
        if (allStreamsDB.isNotEmpty()) {
            view.showData(allStreamsDB)
            loadRemoteStreams()
        } else {
            loadStreamsFromDatabase()
        }
    }

    private fun loadStreamsFromDatabase() {
        loaderStreams.getAllStreamsFromDB().subscribe(
            { subStreams ->
                if (subStreams.isNotEmpty()) {
                    allStreams = subStreams as MutableList<ViewTyped>
                    allStreamsDB = subStreams
                    view.showData(allStreams)
                }
            },
            { error ->
                view.showMessage(res.getString(R.string.msg_database_error), error.message.toString())
            },
            {
                view.showMessage(
                    res.getString(R.string.msg_database_error),
                    res.getString(R.string.msg_database_empty)
                )
            })
            .disposeOnFinish()
        loadRemoteStreams()
    }

    private fun loadSubStreamsFromDatabase() {
        loaderStreams.getSubStreamsFromDB().subscribe(
            { result ->
                if (result.isNotEmpty()) {
                    subStreams = result as MutableList<ViewTyped>
                }
            },
            { error ->
                Log.i(res.getString(R.string.msg_database_error), error.message.toString())
            },
            {
                Log.i(
                    res.getString(R.string.msg_database_error),
                    res.getString(R.string.msg_database_empty)
                )
            })
            .disposeOnFinish()
        loadRemoteStreams()
    }

    private fun loadRemoteStreams() {
        view.showLoading("")
        loaderStreams.getRemoteAllStreams().subscribe(
            { result ->
                allStreams = result as MutableList<ViewTyped>
                view.showData(allStreams)
            },
            { error ->
                if (allStreamsDB.isEmpty()) {
                    view.showError(error.message)
                } else {
                    view.showData(allStreamsDB)
                }
            }
        ).disposeOnFinish()
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
                    val filteredStreams = allStreams.filter { stream ->
                        val item = stream as TitleUi
                        item.title.contains(inputText, ignoreCase = true)
                    }
                    if (filteredStreams.isEmpty()) {
                        view.showData(listOf(EmptyView))
                    } else {
                        view.showData(filteredStreams)
                    }
                },
                { error -> view.showData(allStreams) }
            ).disposeOnFinish()
            if (text.isNullOrEmpty()) {
                view.showData(allStreams)
            }
        }
    }

    private fun resultStreams(allStreams: MutableList<ViewTyped>, subStreams: MutableList<ViewTyped>) =
        allStreams.map { allStream ->
            if (subStreams.contains(allStream)) {
                (allStream as AllStreamsUi).isChecked = true
            }
        }

    companion object {
        private const val TIME_SEARCH_DELAY = 500L
    }
}