package com.volboy.courseproject.presentation.profile

import com.volboy.courseproject.model.OwnUser
import com.volboy.courseproject.presentation.mvp.view.LoadErrorView

interface ProfileView : LoadErrorView {
    fun showData(user: OwnUser)
}