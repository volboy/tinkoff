package com.volboy.courseproject.presentation.mvp.presenter.addstream

import com.google.gson.Gson
import com.volboy.courseproject.App
import com.volboy.courseproject.R
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.model.LoaderStreams
import com.volboy.courseproject.model.Request
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import javax.inject.Inject

class AddStreamPresenter : RxPresenter<AddStreamView>(AddStreamView::class.java) {

    @Inject
    lateinit var loaderStreams: LoaderStreams

    @Inject
    lateinit var res: ResourceProvider

    init {
        App.component.injectLoaderStreams(this)
        App.component.injectResourceProvider(this)
    }

    fun createNewStream(name: String, description: String, inviteOnly: Boolean) {
        view.showLoading()
        val request = listOf(Request(name, description))
        val gson = Gson()
        val requestJSON = gson.toJson(request)
        val createNewStream = loaderStreams.subscribeToStream(requestJSON, inviteOnly)
        createNewStream.subscribe(
            { result ->
                val countSubscribed = result.subscribed.keys.size
                val countAlreadySub = result.alreadySubscribed.keys.size
                if (countSubscribed != 0) {
                    view.showData(
                        res.getString(R.string.stream_was_created_str),
                        res.getString(R.string.count_of_subs_str) + countSubscribed
                    )
                } else if (countAlreadySub != 0) {
                    view.showData(
                        res.getString(R.string.error_str),
                        res.getString(R.string.stream_already_was_str)
                    )
                } else {
                    view.showData(
                        res.getString(R.string.something_wrong),
                        res.getString(R.string.error_str)
                    )
                }
            },
            { error ->
                view.showData(
                    res.getString(R.string.error_str),
                    error.message
                )
            }
        ).disposeOnFinish()
    }
}