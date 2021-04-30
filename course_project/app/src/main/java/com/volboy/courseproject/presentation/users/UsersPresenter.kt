package com.volboy.courseproject.presentation.users

import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.volboy.courseproject.App.Companion.loaderUsers
import com.volboy.courseproject.App.Companion.resourceProvider
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.recyclerview.ViewTyped
import com.volboy.courseproject.recyclerview.simpleitems.EmptyView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class UsersPresenter : RxPresenter<UsersView>(UsersView::class.java) {
    private lateinit var searchText: Observable<String>
    private var data = mutableListOf<ViewTyped>()

    fun getUsers() {
        view.showLoading("")
        loadRemoteUsers()
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
                    val filteredStreams = data.filter { users ->
                        val item = users as PeopleUi
                        item.name.contains(inputText, ignoreCase = true)
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

    private fun loadRemoteUsers() {
        val users = loaderUsers.getRemoteUsers()
        users.subscribe(
            { result ->
                data = result
                view.showData(data)
                writeLog(resourceProvider.getString(R.string.msg_network_ok))
            },
            { error ->
                view.showError(error.message)
                writeLog(resourceProvider.getString(R.string.msg_network_error))
            }
        ).disposeOnFinish()
    }

    private fun writeLog(msg: String) {
        Log.i(resourceProvider.getString(R.string.log_string), msg)
    }

    companion object {
        private const val TIME_SEARCH_DELAY = 500L
    }
}