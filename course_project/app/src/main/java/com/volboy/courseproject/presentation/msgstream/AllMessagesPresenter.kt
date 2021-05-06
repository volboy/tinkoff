package com.volboy.courseproject.presentation.msgstream

import com.volboy.courseproject.App
import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.model.LoaderMessage
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import com.volboy.courseproject.recyclerview.ViewTyped
import javax.inject.Inject

class AllMessagesPresenter : RxPresenter<AllMessagesView>(AllMessagesView::class.java) {
    private lateinit var data: MutableList<ViewTyped>

    @Inject
    lateinit var loaderMessages: LoaderMessage

    @Inject
    lateinit var res: ResourceProvider

    init {
        App.component.injectLoaderMessages(this)
        App.component.injectResourceProvider(this)
    }

    fun loadMessageOfStream(streamName: String) {

    }


}