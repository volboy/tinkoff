package com.volboy.courseproject

import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import javax.inject.Inject

class MainPresenter : RxPresenter<MainView>(MainView::class.java) {
    private var transaction = 0

    @Inject
    lateinit var loaderUsers: LoaderUsers

    init {
        App.component.injectLoaderUsers(this)
    }

    fun saveInstance(transaction: Int) {
        this.transaction = transaction
    }

    fun restoreInstance(): Int {
        return transaction
    }

    fun getOwnId() {
        val ownUser = loaderUsers.getOwnUser()
        ownUser.subscribe(
            { result ->
                ownId = result.user_id
            },
            { error ->
                //TODO Сообщение об ошибке
            }
        ).disposeOnFinish()
    }

    companion object {
        var ownId = 0
    }
}

