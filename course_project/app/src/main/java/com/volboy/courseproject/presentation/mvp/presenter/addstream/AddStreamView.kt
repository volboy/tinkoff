package com.volboy.courseproject.presentation.mvp.presenter.addstream

interface AddStreamView {
    fun showData(title: String, msg: String?)

    fun showLoading()
}