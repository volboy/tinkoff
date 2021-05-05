package com.volboy.courseproject.presentation.streams.allstreams

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.volboy.courseproject.R
import com.volboy.courseproject.presentation.streams.UiHolderFactory
import com.volboy.courseproject.recyclerview.BaseViewHolder
import com.volboy.courseproject.recyclerview.ViewTyped

class AllStreamsUi(
    val title: String,
    var isChecked: Boolean = false,
    override val viewType: Int,
    override var uid: String
) : ViewTyped

class AllStreamsViewHolder(val view: View, private val channelsInterface: UiHolderFactory.ChannelsInterface) :
    BaseViewHolder<AllStreamsUi>(view) {
    private val title: TextView = view.findViewById(R.id.streamText)
    private val switch: SwitchCompat = view.findViewById(R.id.streamSwitch)

    override fun bind(item: AllStreamsUi) {
        view.setOnClickListener {
            channelsInterface.getClickedView(view, adapterPosition, item.viewType)
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            channelsInterface.getCheckedSwitch(isChecked, adapterPosition)
        }
        title.text = item.title
        switch.isChecked = item.isChecked
    }
}