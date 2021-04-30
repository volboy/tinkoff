package com.volboy.courseproject.presentation.bottomfragment

import com.volboy.courseproject.recyclerview.BaseAdapter
import com.volboy.courseproject.recyclerview.HolderFactory
import com.volboy.courseproject.recyclerview.ViewTyped

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
