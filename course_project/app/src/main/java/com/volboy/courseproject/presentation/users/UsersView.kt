package com.volboy.courseproject.presentation.users

import com.volboy.courseproject.presentation.mvp.view.LoadErrorView
import com.volboy.courseproject.recyclerview.ViewTyped

interface UsersView : LoadErrorView {

    fun showData(data: List<ViewTyped>)

    fun showUsersStatus(userId:Int, status: String)
}