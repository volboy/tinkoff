package com.volboy.courseproject.presentation.msgstream

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView
import com.volboy.courseproject.recyclerview.ViewTyped

interface AllMessagesView : LoadErrorView {
    fun showMessage(data: List<ViewTyped>, position: Int)

    fun updateMessage(data: List<ViewTyped>, msgPosition: Int)

    fun sendMessage(data: List<ViewTyped>, msgPosition: Int)
}
