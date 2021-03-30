package com.volboy.course_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.ui.MainFragment
import io.reactivex.Observable

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()
    private lateinit var searchText: Observable<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        toolBarSearch()
        val searchToolbar = binding.searchToolbar
        setSupportActionBar(searchToolbar)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mainFragment)
        transaction.commit()
        setContentView(binding.root)
    }

    private fun toolBarSearch() = binding.searchEditText.addTextChangedListener { text ->
        searchText = Observable.create { emitter ->
            emitter.onNext(text.toString())
        }
       /* searchText
            .filter { inputText -> inputText.isNotEmpty() }
            .distinctUntilChanged()
            .subscribe(
                { inputText ->
                    val filteredUsers = mutableUsers.filter { user -> user.name.contains(inputText) || user.surname.contains(inputText) }
                    usersAdapter.update(filteredUsers)
                },
                { error -> showError(error) },
                { onCompleteLog() }
            )*/
    }
}


