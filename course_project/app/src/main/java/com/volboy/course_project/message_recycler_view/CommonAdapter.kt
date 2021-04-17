package com.volboy.course_project.message_recycler_view

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil

class CommonAdapter<T : ViewTyped>(
    holderFactory: HolderFactory,
    diffItemCallback: DiffUtil.ItemCallback<T>
) : BaseAdapter<T>(holderFactory) {

    private val differ = AsyncListDiffer(this, diffItemCallback)

    override var items: List<T>
        get() = differ.currentList
        set(newItems) = differ.submitList(newItems)
}