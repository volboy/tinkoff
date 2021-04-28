package com.volboy.course_project.presentation.streams

import android.view.View
import com.volboy.course_project.R
import com.volboy.course_project.presentation.users.PeopleViewHolder
import com.volboy.course_project.recyclerview.BaseViewHolder
import com.volboy.course_project.recyclerview.HolderFactory

class UiHolderFactory(private val channelsInterface: ChannelsInterface) : HolderFactory() {
    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_collapse -> AllStreamsViewHolder(view, channelsInterface)
            R.layout.item_expand -> AllStreamsViewHolder(view, channelsInterface)
            R.layout.item_people_list -> PeopleViewHolder(view, channelsInterface)
            else -> null
        }
    }
    interface ChannelsInterface{
        fun getClickedView(view:View, position: Int, viewType:Int)
    }
}