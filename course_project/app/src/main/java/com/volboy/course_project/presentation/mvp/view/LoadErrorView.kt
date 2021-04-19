package com.volboy.course_project.presentation.mvp.view

interface LoadErrorView {
    fun showLoading(msg:String)

    fun showError(error:String?)
}