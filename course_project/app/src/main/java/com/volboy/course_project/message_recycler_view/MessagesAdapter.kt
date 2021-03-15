package com.volboy.course_project.message_recycler_view

import androidx.recyclerview.widget.AsyncListDiffer

class MessagesAdapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {

    private val localItems: MutableList<T> =mutableListOf()
    private val differ=AsyncListDiffer(this, MessageDiffUtilCallback())

    override var items: List<T>
        get() = localItems
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }
}