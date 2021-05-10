package com.volboy.courseproject

import com.volboy.courseproject.common.ResourceProvider
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.mvp.presenter.base.RxPresenter
import javax.inject.Inject

class MainPresenter : RxPresenter<MainView>(MainView::class.java) {

    @Inject
    lateinit var loaderUsers: LoaderUsers

    @Inject
    lateinit var res: ResourceProvider

    init {
        App.component.injectLoaderUsers(this)
        App.component.injectResourceProvider(this)
    }

    fun getOwnId() {
        view.showLoading()
        val ownUser = loaderUsers.getOwnUser()
        ownUser.subscribe(
            { result ->
                view.continueWork(result.user_id)
                ownId = result.user_id
            },
            { error ->
                view.showInfo(res.getString(R.string.something_wrong), error.toString())
            }
        ).disposeOnFinish()
    }

    companion object {
        var ownId = 0
    }
}
