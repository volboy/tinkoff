package com.volboy.courseproject

import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import javax.inject.Inject

class MainPresenter : RxPresenter<MainView>(MainView::class.java) {

    @Inject
    lateinit var loaderUsers: LoaderUsers

    init {
        App.component.injectLoaderUsers(this)
    }

    fun getOwnId() {
        val ownUser = loaderUsers.getOwnUser()
        ownUser.subscribe(
            { result ->
                view.continueWork(result.user_id)
            },
            { error ->
                view.showError(error.toString())
            }
        ).disposeOnFinish()
    }

    companion object {
        //TODO Переделать
        var ownId = 0
    }
}

