package com.volboy.courseproject.presentation.mvp.presenter.addstream

import com.volboy.courseproject.model.OwnUser
import com.volboy.courseproject.presentation.mvp.view.LoadErrorView

interface AddStreamView : LoadErrorView {
    fun showData(user: OwnUser)
}