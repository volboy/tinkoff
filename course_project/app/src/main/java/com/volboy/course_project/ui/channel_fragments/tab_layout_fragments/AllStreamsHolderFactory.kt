package com.volboy.course_project.ui.channel_fragments.tab_layout_fragments

import android.view.View
import com.volboy.course_project.R
import com.volboy.course_project.message_recycler_view.BaseViewHolder
import com.volboy.course_project.message_recycler_view.HolderFactory

class AllStreamsHolderFactory(private val channelsInterface: ChannelsInterface) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.collapse_item -> AllStreamsViewHolder(view, channelsInterface )
            R.layout.expand_item -> AllStreamsViewHolder(view, channelsInterface)
            else -> null
        }
    }
    interface ChannelsInterface{
        fun getClickedView(view:View, position: Int, viewType:Int)
    }
}