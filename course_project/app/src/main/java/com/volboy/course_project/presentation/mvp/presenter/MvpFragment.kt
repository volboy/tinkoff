package com.volboy.course_project.presentation.mvp.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.volboy.course_project.presentation.mvp.presenter.base.Presenter

abstract class MvpFragment<View, P : Presenter<View>> : Fragment(),
    MvpViewCallback<View, P> {

    private val mvpHelper: MvpHelper<View, P> by lazy {
        MvpHelper(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mvpHelper.create()

    }

    override fun onStop() {
        super.onStop()
        val isFinishing = isRemoving || requireActivity().isFinishing
        mvpHelper.destroy(isFinishing)
        super.onDestroyView()
    }
}