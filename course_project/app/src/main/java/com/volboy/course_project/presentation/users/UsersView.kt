package com.volboy.course_project.presentation.users

import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.presentation.mvp.view.LoadErrorView

interface UsersView : LoadErrorView {

    fun showData(data: List<ViewTyped>)
}