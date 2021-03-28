package com.volboy.course_project.model

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import io.reactivex.Single

class ObservableStreams {

    fun getStreams(): Single<List<ViewTyped>> =
        Single.fromCallable { generateStreams() }
            .map { streams -> viewTypedStreams(streams) }

    private fun generateStreams(): List<Stream> {
        return listOf(
            Stream("#general", listOf(Pair("Testing", 1240), Pair("Bruh", 24))),
            Stream("#Desing", null),
            Stream("#PR", null)
        )
    }

    private fun viewTypedStreams(streams: List<Stream>): List<ViewTyped> {
        val typedList = mutableListOf<ViewTyped>()
        streams.forEach { item ->
            val uid=item.streamName+item.topics?.size
            typedList.add(TitleUi(item.streamName, item.topics?.size.toString(), item.topics, R.drawable.ic_arrow_down, R.layout.item_collapse, uid))
        }
        return typedList
    }
}



