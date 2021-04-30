package com.volboy.courseproject.presentation.mvp.presenter

import com.volboy.courseproject.presentation.mvp.presenter.base.Presenter

interface MvpViewCallback<View, P: Presenter<View>> {

    fun getPresenter(): P

    fun getMvpView(): View

}