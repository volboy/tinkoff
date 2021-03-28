package com.volboy.course_project.message_recycler_view

import android.view.View
import com.volboy.course_project.R

class MessageHolderFactory(private val click: (View) -> Boolean) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_in_message -> MessageViewHolder(view, click)
            R.layout.item_out_message -> MessageViewHolder(view, click)
            R.layout.item_date_divider -> DataViewHolder(view)
            else -> null
        }
    }
}