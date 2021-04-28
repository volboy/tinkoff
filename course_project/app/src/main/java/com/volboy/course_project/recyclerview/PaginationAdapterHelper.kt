package com.volboy.course_project.recyclerview

class PaginationAdapterHelper(private val onLoadNextMessageCallback: (offset: Int) -> Unit) {

    fun onBind(adapterPosition: Int, itemsSize: Int) {
        if (adapterPosition == itemsSize - DEFAULT_END_POSITION)
            onLoadNextMessageCallback(adapterPosition)
    }

    companion object {
        private const val DEFAULT_END_POSITION = 5
    }
}