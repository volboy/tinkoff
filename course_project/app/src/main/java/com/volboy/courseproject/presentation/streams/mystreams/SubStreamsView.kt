package com.volboy.courseproject.presentation.streams.mystreams

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView
import com.volboy.courseproject.recyclerview.ViewTyped

interface SubStreamsView : LoadErrorView {
    fun showData(data: List<ViewTyped>)

    fun updateData(data: List<ViewTyped>, position: Int)

    fun hideData(data: List<ViewTyped>)

    fun showMessage(title: String, msg: String)
}
