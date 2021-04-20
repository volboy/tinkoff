package com.volboy.course_project.presentation.details

import com.volboy.course_project.presentation.mvp.view.LoadErrorView

interface DetailsView : LoadErrorView {
    fun showStatus(status: String)
}