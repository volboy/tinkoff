package com.volboy.courseproject

import android.content.Context
import android.content.SharedPreferences
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.os.Bundle
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.databinding.ActivityMainBinding
import com.volboy.courseproject.presentation.main.MainFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpActivity
import javax.inject.Inject


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()

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
        supportActionBar?.hide()
        val pref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        val isFirstRun = pref.getBoolean(FIRST_RUN_APP, true)
        val ownUserId = pref.getInt(USER_PREF_KEY, 0)
        if (isFirstRun && !isConnected()) {
            //TODO Сообщение с невоможностью дальнейшей работы
        } else if (!isFirstRun && !isConnected()) {
            //TODO Уведомление о не актуальности данных
        } else if (!isFirstRun && isConnected() && ownUserId == 0) {
            mainPresenter.getOwnId()
        } else if (!isFirstRun && isConnected() && ownUserId != 0) {
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mainFragment)
                    .commit()
            }
        }
        editor.putBoolean("KEY_BOOLEAN", true)
        setContentView(binding.root)
    }

    override fun continueWork(ownId: Int) {
        val pref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(USER_PREF_KEY, ownId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, mainFragment)
            .commit()
    }

    override fun showError(error: String) {
        //TODO Уведомление о не возможности получения собственного id
    }


    fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            for (network in connectivityManager.allNetworks) {
                val capabilities = connectivityManager.getNetworkCapabilities(network)
                if (capabilities?.hasCapability(NET_CAPABILITY_INTERNET) == true) {
                    return true
                }
            }
            return false
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }

    override fun getPresenter(): MainPresenter = mainPresenter

    override fun getMvpView(): MainView = this

    companion object {
        const val SHARED_PREF_NAME = "ZULIP_APP_PREF"
        const val USER_PREF_KEY = "ID_OF_CURRENT_USER"
        const val FIRST_RUN_APP = "FIRST_RUN_APP"

    }
}

