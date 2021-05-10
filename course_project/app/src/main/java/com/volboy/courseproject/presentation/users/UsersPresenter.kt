package com.volboy.courseproject.presentation.users

import android.util.Log
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.recyclerview.ViewTyped
import com.volboy.courseproject.recyclerview.simpleitems.EmptyView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsersPresenter : RxPresenter<UsersView>(UsersView::class.java) {
    private lateinit var searchText: Observable<String>
    private var data = mutableListOf<ViewTyped>()

    @Inject
    lateinit var loaderUsers: LoaderUsers

    @Inject
    lateinit var res: ResourceProvider

    init {
        component.injectLoaderUsers(this)
        component.injectResourceProvider(this)
    }

    fun getUsers() {
        view.showLoading("")
        loadRemoteUsers()
    }

    fun setStatusObservable() {
        val userForGetStatus = Observable.fromIterable(data)
        userForGetStatus
            .zipWith(
                Observable.interval(TIME_STATUS_DELAY, TimeUnit.MILLISECONDS),
                BiFunction { item: ViewTyped, _: Long -> item })
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user -> getUserStatus(user.uid.toInt()) },
                { error -> Log.i(res.getString(R.string.log_string), error.message.toString()) },
            )
            .disposeOnFinish()
    }

    private fun getUserStatus(userId: Int) {
        loaderUsers.getUserStatus(userId).subscribe(
            { result ->
                view.showUsersStatus(userId, result.presence.aggregated.status)
            },
            { error -> Log.i(res.getString(R.string.log_string), error.message.toString()) },
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
                { view.showData(data) }
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

    companion object {
        private const val TIME_SEARCH_DELAY = 500L
        private const val TIME_STATUS_DELAY = 500L
    }
}
