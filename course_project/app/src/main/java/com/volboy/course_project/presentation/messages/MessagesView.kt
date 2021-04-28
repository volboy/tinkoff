package com.volboy.course_project.presentation.messages

import com.volboy.course_project.presentation.mvp.view.LoadErrorView
import com.volboy.course_project.recyclerview.ViewTyped

interface MessagesView : LoadErrorView {
    fun showMessage(data: List<ViewTyped>)

    fun updateMessage(data: List<ViewTyped>, msgPosition: Int)

    fun sendMessage(message: ViewTyped)

    fun showLocalError()

    fun showLocalLoading()

}