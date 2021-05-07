package com.volboy.courseproject.presentation.msgstream

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView
import com.volboy.courseproject.recyclerview.ViewTyped

interface MessagesOfStreamView : LoadErrorView {
    fun showMessage(data: List<ViewTyped>, position: Int)

    fun showInfo(title: String, msg: String)

    fun updateMessage(data: List<ViewTyped>, msgPosition: Int)

    fun sendMessage(data: List<ViewTyped>, msgPosition: Int)
}
