package com.volboy.course_project.ui.channel_fragments.all_streams

import android.view.View
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.DataViewHolder
import com.volboy.course_project.message_recycler_view.HolderFactory
import com.volboy.course_project.message_recycler_view.MessageViewHolder

class AllStreamsHolderFactory(private val click: (View) -> Unit) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.collapse_item -> AllStreamsViewHolder(view, click)
            R.layout.expand_item -> AllStreamsViewHolder(view, click)
            else -> null
        }
    }
}