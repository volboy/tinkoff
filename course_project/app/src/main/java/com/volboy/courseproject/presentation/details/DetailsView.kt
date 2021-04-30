package com.volboy.courseproject.presentation.details

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView

interface DetailsView : LoadErrorView {
    fun showStatus(status: String)
}