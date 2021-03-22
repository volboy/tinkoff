package com.volboy.course_project.model

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.channel_fragments.all_streams.TitleUi
import java.io.Serializable

class LoaderStreams {
    private lateinit var listStreams: MutableList<Stream>
    private var typedList = mutableListOf<ViewTyped>()

    private fun loadStreams() {
        listStreams = mutableListOf<Stream>(
            Stream("#general", listOf(Pair("Testing", 1240), Pair("Bruh", 24))),
            Stream("#Desing", null),
            Stream("#PR", null)
        )
    }

    fun convertStreams(): MutableList<ViewTyped> {
        loadStreams()
        listStreams.forEach { item ->
            typedList.add(TitleUi(item.streamName, null, R.drawable.ic_arrow_down, R.layout.collapse_item, item.streamName))

        }
        return typedList
    }

    fun addExpandItem(position: Int, isSelected: Boolean): MutableList<ViewTyped> {
        val itemInPosition = listStreams[position]
        if (isSelected) {
            typedList.removeAt(position)
            typedList.add(position, TitleUi(itemInPosition.streamName, null, R.drawable.ic_arrow_up, R.layout.collapse_item, itemInPosition.streamName))
            if (itemInPosition.topics != null) {
                itemInPosition.topics.forEach { topic ->
                    typedList.add(TitleUi(topic.first, topic.second.toString() + " mes", null, R.layout.expand_item, topic.first))
                }
            }
        } else {
            typedList.removeAt(position)
            typedList.add(position, TitleUi(itemInPosition.streamName, null, R.drawable.ic_arrow_down, R.layout.collapse_item, itemInPosition.streamName))
            if (itemInPosition.topics != null) {
                itemInPosition.topics.forEach { _ ->
                    typedList.removeAt(position + 1)
                }
            }
        }

        return typedList
    }
}



