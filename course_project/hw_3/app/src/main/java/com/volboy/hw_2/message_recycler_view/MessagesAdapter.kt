package com.volboy.hw_2.message_recycler_view

class MessagesAdapter<T : ViewTyped>(holderFactory: HolderFactory) : BaseAdapter<T>(holderFactory) {

    private val localItems: MutableList<T> =mutableListOf()

    override var items: List<T>
        get() = localItems
        set(newItems) {
            localItems.clear()
            localItems.addAll(newItems)
            notifyDataSetChanged()
        }
}