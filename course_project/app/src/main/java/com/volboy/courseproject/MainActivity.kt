package com.volboy.courseproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.volboy.courseproject.App.Companion.component
import com.volboy.courseproject.databinding.ActivityMainBinding
import com.volboy.courseproject.model.LoaderUsers
import com.volboy.courseproject.presentation.main.MainFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var loaderUsers: LoaderUsers

    init {
        component.injectLoaderUsers(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val searchToolbar = binding.searchToolbar
        setSupportActionBar(searchToolbar)
        getOwnId()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, mainFragment)
            .commit()
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun getOwnId() {
        val ownUser = loaderUsers.getOwnUser()
        compositeDisposable.add(ownUser.subscribe(
            { result ->
                ownId = result.user_id
            },
            { error ->
                Toast.makeText(applicationContext, resources.getString(R.string.msg_network_error) + error.message, Toast.LENGTH_SHORT).show()
            }
        ))
    }

    companion object {
        var ownId = 0
    }
}
