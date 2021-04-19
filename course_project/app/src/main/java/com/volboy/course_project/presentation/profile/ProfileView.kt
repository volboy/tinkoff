package com.volboy.course_project.presentation.profile

import com.volboy.course_project.model.OwnUser
import com.volboy.course_project.presentation.mvp.view.LoadErrorView

interface ProfileView : LoadErrorView {
    fun showData(user: OwnUser)
}