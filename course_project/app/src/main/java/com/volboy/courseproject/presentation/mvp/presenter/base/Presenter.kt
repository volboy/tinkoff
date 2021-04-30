package com.volboy.courseproject.presentation.mvp.presenter.base

interface Presenter<View> {

    fun attachView(view: View)

    fun detachView(isFinishing: Boolean)
}