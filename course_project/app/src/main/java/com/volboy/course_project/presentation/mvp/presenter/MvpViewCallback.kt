package com.volboy.course_project.presentation.mvp.presenter

import com.volboy.course_project.presentation.mvp.presenter.base.Presenter

interface MvpViewCallback<View, P: Presenter<View>> {

    fun getPresenter(): P

    fun getMvpView(): View

}