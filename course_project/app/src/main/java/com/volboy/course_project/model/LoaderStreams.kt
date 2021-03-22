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
            Stream("#general", listOf(Pair("Testing", 1240))),
            Stream("#Desing", null),
            Stream("#PR", null)
        )
    }

    fun convertStreams(selectedItem: Int, isSelected:Boolean): MutableList<ViewTyped> {
        loadStreams()
        if (isSelected) {
            typedList.clear()
            listStreams.forEach { item ->
                typedList.add(TitleUi(item.streamName, null, R.drawable.ic_arrow_up, R.layout.collapse_item, item.streamName))
            }
            val selectedListTopic = listStreams[selectedItem].topics
            selectedListTopic?.forEach { item ->
                typedList.add(selectedItem+1, TitleUi(item.first, item.second.toString() + " mes", null, R.layout.expand_item, item.first))

            }
        } else {
            typedList.clear()
            listStreams.forEach { item ->
                typedList.add(TitleUi(item.streamName, null, R.drawable.ic_arrow_down, R.layout.collapse_item, item.streamName))
            }
        }
        return typedList
    }
}


