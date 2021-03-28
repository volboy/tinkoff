package com.volboy.course_project.model

import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.ViewTyped
import com.volboy.course_project.ui.people_fragments.PeopleUi
import io.reactivex.Single

class ObservablePeople {

    fun getPeople(): Single<List<ViewTyped>> =
        Single.fromCallable { generatePeople() }
            .map { people -> viewTypedPeople(people) }

    private fun generatePeople(): List<People> {
        return listOf(
            People("Ivan Ivanov", "ivanov@gmail.com", R.drawable.darell_steward),
            People("Darell Steward", "darell@gmail.com", R.drawable.profile_image)
        )
    }

    private fun viewTypedPeople(streams: List<People>): List<ViewTyped> {
        val typedList = mutableListOf<ViewTyped>()
        streams.forEach { item ->
            typedList.add(PeopleUi(item.name, item.email, item.imageId, R.layout.item_people_list, item.name))
        }
        return typedList
    }
}



