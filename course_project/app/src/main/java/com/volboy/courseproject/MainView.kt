package com.volboy.courseproject

interface MainView {
    fun continueWork(ownId: Int)

    fun showInfo(title: String, msg: String)

    fun showLoading()
}