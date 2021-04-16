package com.volboy.course_project.presentation.mvp.presenter.base

interface Presenter<View> {

    fun attachView(view: View)

    fun detachView(isFinishing: Boolean)
}