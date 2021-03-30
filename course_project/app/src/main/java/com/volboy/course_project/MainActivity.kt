package com.volboy.course_project

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.volboy.course_project.databinding.ActivityMainBinding
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.MainFragment
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import io.reactivex.Observable

class MainActivity : AppCompatActivity(), FragmentsCallBack {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment = MainFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val searchToolbar = binding.searchToolbar
        setSupportActionBar(searchToolbar)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mainFragment)
        transaction.commit()
        setContentView(binding.root)
    }

    override fun searchToolbar(list: List<ViewTyped>): List<ViewTyped> {
        var outputList = listOf<ViewTyped>()
        var searchText: Observable<String>
        val searchEdit = binding.searchEditText
        searchEdit.addTextChangedListener { text ->
            searchText = Observable.create { emitter ->
                emitter.onNext(text.toString())
            }
            searchText
                .filter { inputText -> inputText.isNotEmpty() }
                .filter { inputText -> inputText[0] != ' ' }
                .distinctUntilChanged()
                .subscribe(
                    { inputText ->
                        val filteredStreams = list.filter { stream ->
                            val item = stream as TitleUi
                            item.title.contains(inputText)
                        }
                        if (filteredStreams.isEmpty()) {
                            outputList = listOf(TitleUi("Ничего не найдено", null, null, 0, R.layout.item_collapse, ""))
                        } else {
                            outputList = filteredStreams
                        }
                    },
                    { error -> Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show() }
                )
            if (text.isNullOrEmpty()) {
                outputList = list
            }
        }
        return outputList
    }
}


