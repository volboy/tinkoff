package com.volboy.courseproject

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.databinding.ActivityMainBinding
import com.volboy.courseproject.presentation.bottominfo.BottomInfoFragment
import com.volboy.courseproject.presentation.main.MainFragment
import com.volboy.courseproject.presentation.mvp.presenter.MvpActivity
import javax.inject.Inject


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()
    private var network = false

    @Inject
    lateinit var mainPresenter: MainPresenter

    init {
        component.injectMainPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        network = isConnected()
        val searchToolbar = binding.searchToolbar
        setSupportActionBar(searchToolbar)
        supportActionBar?.hide()
        setContentView(binding.root)
        val pref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        val isFirstRun = pref.getBoolean(FIRST_RUN_APP, true)
        val ownUserId = pref.getInt(USER_PREF_KEY, EMPTY_USER_OWN_ID)
        if (isFirstRun) {
            if (network) {
                getPresenter().getOwnId()
            } else {
                showError(getString(R.string.something_wrong), getString(R.string.first_run_str))
            }
        } else {
            if (network) {
                getPresenter().getOwnId()
            } else {
                if (ownUserId == EMPTY_USER_OWN_ID) {
                    showInfo(getString(R.string.something_wrong), getString(R.string.first_run_str))
                } else {
                    showInfo(getString(R.string.msg_network_error), getString(R.string.offline_run_str))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, mainFragment)
                        .commit()
                }
            }
        }
        editor.putBoolean(FIRST_RUN_APP, false)
        editor.apply()
        editor.commit()
    }

    override fun continueWork(ownId: Int) {
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isGone = true
        binding.container.isVisible = true
        val pref: SharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(USER_PREF_KEY, ownId)
        editor.apply()
        editor.commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, mainFragment)
            .commit()
    }

    override fun showInfo(title: String, msg: String) {
        val bottomInfoFragment = BottomInfoFragment()
        bottomInfoFragment.arguments = bundleOf(
            BottomInfoFragment.ARG_INFO_TITLE to title,
            BottomInfoFragment.ARG_INFO_TEXT to msg
        )
        bottomInfoFragment.show(supportFragmentManager, bottomInfoFragment.tag)
    }

    override fun showLoading() {
        binding.container.isGone = true
        binding.fragmentError.root.isGone = true
        binding.fragmentLoading.root.isVisible = true
    }

    fun showError(title: String, msg: String) {
        binding.container.isGone = true
        binding.fragmentLoading.root.isGone = true
        binding.fragmentError.root.isVisible = true
        binding.fragmentError.errorTitle.text = title
        binding.fragmentError.errorText.text = msg
        binding.fragmentError.retryText.setOnClickListener {
            if (isConnected()) {
                getPresenter().getOwnId()
            } else {
                showInfo(getString(R.string.msg_network_error), getString(R.string.check_network_str))
            }
        }
    }


    private fun isConnected(): Boolean {
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        const val EMPTY_USER_OWN_ID = 0

    }
}

