package com.volboy.course_project.presentation.search

import com.volboy.course_project.recyclerview.ViewTyped

class SearchHelper(private val onSearchCallback: (data: MutableList<ViewTyped>, filter: String) -> Unit) {

    fun makeSearch(data: MutableList<ViewTyped>, filter: String) {
        onSearchCallback(data, filter)
    }
}