package com.volboy.course_project.ui.people_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.volboy.course_project.R
import com.volboy.course_project.databinding.FragmentPeopleBinding
import com.volboy.course_project.message_recycler_view.CommonAdapter
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.model.People
import com.volboy.course_project.ui.channel_fragments.MessagesFragment
import com.volboy.course_project.ui.channel_fragments.tab_layout_fragments.uiHolderFactory

class PeopleFragment : Fragment(), uiHolderFactory.ChannelsInterface {
    private lateinit var binding: FragmentPeopleBinding
    private var listPeople = mutableListOf<People>()
    private var typedList = mutableListOf<ViewTyped>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        typedList.clear()
        binding = FragmentPeopleBinding.inflate(inflater, container, false)
        listPeople = mutableListOf(
            People("Ivan Ivanov", "ivanov@gmail.com", R.drawable.darell_steward),
            People("Petr Petrov", "petrov@gmail.com", R.drawable.profile_image)
        )
        listPeople.forEach { item ->
            typedList.add(PeopleUi(item.name, item.email, item.imageId, R.layout.people_list_item, item.name))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val clickListener: (View) -> Unit = { view ->

        }
        val holderFactory = uiHolderFactory(this)
        val commonAdapter = CommonAdapter<ViewTyped>(holderFactory)
        commonAdapter.items = typedList
        binding.rwPeople.adapter = commonAdapter
    }

    override fun getClickedView(view: View, position: Int, viewType: Int) {
        val detailsPeopleFragment = DetailsPeopleFragment()
        val arguments = Bundle()
        arguments.putString(ARG_NAME, listPeople[position].name)
        arguments.putString(ARG_EMAIL, listPeople[position].email)
        arguments.putInt(ARG_IMAGE, listPeople[position].imageId)
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