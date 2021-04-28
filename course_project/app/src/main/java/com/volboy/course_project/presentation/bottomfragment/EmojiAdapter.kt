package com.volboy.course_project.presentation.bottomfragment

import com.volboy.course_project.recyclerview.BaseAdapter
import com.volboy.course_project.recyclerview.HolderFactory
import com.volboy.course_project.recyclerview.ViewTyped

class EmojiAdapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {
    private val localItems: MutableList<T> = mutableListOf()
    override var items: List<T>
        get() = localItems
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }
}
