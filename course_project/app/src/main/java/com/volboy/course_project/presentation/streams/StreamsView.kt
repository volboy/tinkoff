package com.volboy.course_project.presentation.streams

import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.mvp.view.LoadErrorView

interface StreamsView : LoadErrorView {
    fun showStreams(data:List<ViewTyped>)

    fun showTopics(data:List<ViewTyped>)

    fun hideTopics(data:List<ViewTyped>)

}
