package com.volboy.courseproject.presentation.mvp.presenter.addstream

import com.google.gson.Gson
import com.volboy.courseproject.App
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

    fun createNewStream(name: String, description: String) {
        val request = listOf(Request(name, description))
        val gson = Gson()
        val narrowsJSON = gson.toJson(request)
        val createNewStream = loaderStreams.subscribeToStream(narrowsJSON)
        createNewStream.subscribe(
            { result -> },
            { error -> }
        ).disposeOnFinish()
    }
}