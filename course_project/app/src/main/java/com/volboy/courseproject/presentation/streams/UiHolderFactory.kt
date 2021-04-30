package com.volboy.courseproject.presentation.streams

import android.view.View
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.users.PeopleViewHolder
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.HolderFactory

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