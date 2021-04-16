package com.volboy.course_project.mvp.presenter

import com.volboy.course_project.message_recycler_view.ViewTyped

interface StreamsView : LoadErrorView {
    fun showStreams(data:List<ViewTyped>)

    fun showTopics(data:List<ViewTyped>)

    fun hideTopics(data:List<ViewTyped>)

}
