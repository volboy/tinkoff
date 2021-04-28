package com.volboy.course_project.presentation.users

import com.volboy.course_project.presentation.mvp.view.LoadErrorView
import com.volboy.course_project.recyclerview.ViewTyped

interface UsersView : LoadErrorView {

    fun showData(data: List<ViewTyped>)
}