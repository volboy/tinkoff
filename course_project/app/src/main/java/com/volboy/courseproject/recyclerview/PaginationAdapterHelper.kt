package com.volboy.courseproject.recyclerview

class PaginationAdapterHelper(private val onLoadNextMessageCallback: (position: Int) -> Unit) {

    fun onBind(adapterPosition: Int, itemsSize: Int) {
        if (adapterPosition == itemsSize - DEFAULT_END_POSITION && itemsSize > 19)
            onLoadNextMessageCallback(adapterPosition)
    }

    companion object {
        private const val DEFAULT_END_POSITION = 5
    }
}