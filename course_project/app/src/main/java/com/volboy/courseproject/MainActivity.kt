package com.volboy.courseproject

import android.os.Bundle
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.databinding.ActivityMainBinding
import com.volboy.courseproject.presentation.main.MainFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpActivity
import javax.inject.Inject


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()
    private var transaction: Int = 0

    @Inject
    lateinit var mainPresenter: MainPresenter

    init {
        component.injectMainPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val searchToolbar = binding.searchToolbar
        setSupportActionBar(searchToolbar)
        mainPresenter.getOwnId()
        if (mainPresenter.restoreInstance() == 0) {
            transaction = supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commit()
            mainPresenter.saveInstance(transaction)
        }
        setContentView(binding.root)
    }

    override fun getPresenter(): MainPresenter = mainPresenter

    override fun getMvpView(): MainView = this
}
