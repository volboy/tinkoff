package com.volboy.course_project.ui.people_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentPeopleBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.ObservablePeople
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.TitleUi
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.UiHolderFactory
import io.reactivex.Observable

class PeopleFragment : Fragment(), UiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentPeopleBinding
    private var listPeople = mutableListOf<ViewTyped>()
    private val loaderPeople=ObservablePeople()
    private lateinit var searchText: Observable<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar;
        mActionBar?.show()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val clickListener: (View) -> Unit = { view ->

        }
        val holderFactory = UiHolderFactory(this)
        val commonAdapter = CommonAdapter<ViewTyped>(holderFactory)
        val observablePeople = loaderPeople.getPeople().subscribe(
            { item ->
                listPeople = item as MutableList<ViewTyped>
                commonAdapter.items = listPeople
            },
            { error -> Snackbar.make(binding.root, error.toString(), Snackbar.LENGTH_LONG).show() }
        )
        binding.rwPeople.adapter = commonAdapter
        val mActionBar = (requireActivity() as AppCompatActivity).supportActionBar;
        mActionBar?.show()
        val searchEdit = requireActivity().findViewById<EditText>(R.id.searchEditText)
        searchEdit.addTextChangedListener { text ->
            searchText = Observable.create { emitter ->
                emitter.onNext(text.toString())
            }
            searchText
                .filter { inputText -> inputText.isNotEmpty() }
                .filter { inputText -> inputText[0]!=' '}
                .distinctUntilChanged()
                .subscribe(
                    { inputText ->
                        val filteredStreams = listPeople.filter { stream ->
                            val item = stream as PeopleUi
                            item.name.contains(inputText)
                        }
                        if (filteredStreams.isEmpty()) {
                            commonAdapter.items = listOf(TitleUi("Ничего не найдено", null, null, 0, R.layout.item_collapse, ""))
                        } else {
                            commonAdapter.items = filteredStreams
                        }
                    },
                    { error -> Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show() }
                )
            if (text.isNullOrEmpty()){
                commonAdapter.items = listPeople
            }
        }

    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        val detailsPeopleFragment = DetailsPeopleFragment()
        val arguments = Bundle()
        var man=listPeople[position] as PeopleUi
        arguments.putString(ARG_NAME, man.name)
        arguments.putString(ARG_EMAIL, man.email)
        arguments.putInt(ARG_IMAGE, man.imageId)
        detailsPeopleFragment.arguments=arguments
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.addToBackStack("FromPeopleFragment")
        transaction.add(R.id.container, detailsPeopleFragment)
        transaction.commit()
    }

    companion object {
        const val ARG_NAME = "name"
        const val ARG_EMAIL = "email"
        const val ARG_IMAGE = "image"
    }
}