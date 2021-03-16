package com.volboy.course_project.message_recycler_view

import androidx.recyclerview.widget.AsyncListDiffer

class MessagesAdapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {

    private val differ = AsyncListDiffer(this, MessageDiffUtilCallback())

    override var items: List<T>
        get() = differ.currentList as List<T>
        set(newItems) {
            differ.submitList(newItems)
        }
}