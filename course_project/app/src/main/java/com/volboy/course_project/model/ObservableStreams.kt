package com.volboy.course_project.model

import android.content.Context
import com.volboy.course_project.App
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ObservableStreams(val context: Context) {


    private fun viewTypedStreams(streamsJSON: List<StreamJSON>): MutableList<ViewTyped> {
        val viewTypedList = mutableListOf<ViewTyped>()
        streamsJSON.forEach { streams ->
            val uid = streams.stream_id.toString()
            viewTypedList.add(TitleUi(streams.name.toString(), null, null, R.drawable.ic_arrow_down, R.layout.item_collapse, uid))
        }
        return viewTypedList
    }

    fun getRemoteStreams(): Single<MutableList<ViewTyped>> {
        return App.instance.zulipApi.getStreams()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { response -> viewTypedStreams(response.streams) }
    }
}



