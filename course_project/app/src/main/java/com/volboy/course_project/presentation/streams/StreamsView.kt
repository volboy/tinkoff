package com.volboy.course_project.presentation.streams

import com.volboy.course_project.presentation.mvp.view.LoadErrorView
import com.volboy.course_project.recyclerview.ViewTyped

interface StreamsView : LoadErrorView {
    fun showData(data: List<ViewTyped>)

    fun hideData(data: List<ViewTyped>)

}
