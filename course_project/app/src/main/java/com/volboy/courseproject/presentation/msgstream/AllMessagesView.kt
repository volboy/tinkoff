package com.volboy.courseproject.presentation.msgstream

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView
import com.volboy.courseproject.recyclerview.ViewTyped

interface AllMessagesView : LoadErrorView {
    fun showData(data: List<ViewTyped>, position: Int)
}
