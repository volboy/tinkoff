package com.volboy.courseproject.presentation.messages

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView
import com.volboy.courseproject.recyclerview.ViewTyped

interface MessagesView : LoadErrorView {
    fun showMessage(data: List<ViewTyped>, position: Int)

    fun updateMessage(data: List<ViewTyped>, msgPosition: Int)

    fun sendMessage(data: List<ViewTyped>, msgPosition: Int)

    fun deleteMessage(data: List<ViewTyped>, msgPosition: Int)

    fun showInfo(title: String, msg: String)
}