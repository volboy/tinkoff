package com.volboy.courseproject.presentation.mvp.view

interface LoadErrorView {
    fun showLoading(msg: String)

    fun showError(error: String?)
}