package com.volboy.course_project.emoji_recycler_view

import com.volboy.course_project.message_recycler_view.BaseAdapter
import com.volboy.course_project.message_recycler_view.HolderFactory
import com.volboy.course_project.message_recycler_view.ViewTyped

class EmojiAdapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {

    private val localItems: MutableList<T> =mutableListOf()

    override var items: List<T>
        get() = localItems
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }
}
