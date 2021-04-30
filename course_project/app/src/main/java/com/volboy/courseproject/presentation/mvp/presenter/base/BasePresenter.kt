package com.volboy.courseproject.presentation.mvp.presenter.base

import androidx.annotation.CallSuper
import com.volboy.courseproject.reflection.ReflectionUtils

abstract class BasePresenter<View> protected constructor(
    viewClass: Class<View>
): Presenter<View> {

    private val stubView: View =
        ReflectionUtils.createStub(viewClass)
    private var realView: View? = null

    val view: View
        get() = realView ?: stubView


    @CallSuper
    override fun attachView(view: View) {
        this.realView = view
    }

    @CallSuper
    override fun detachView(isFinishing: Boolean) {
        realView = null
    }

    fun hasView(): Boolean {
        return view != null
    }
}